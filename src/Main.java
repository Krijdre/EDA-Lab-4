import java.io.*;
import java.util.*;

class Paciente {
    private String nombre;
    private String apellido;
    private String id;
    private int categoria;
    private long tiempoLlegada;
    private String estado;
    private String area;
    private Stack<String> historialCambios;

    public Paciente(String nombre, String apellido, String id, int categoria, String estado, String area,
                    long tiempoLlegada) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.estado = estado;
        this.area = area;
        this.historialCambios = new Stack<>();
        this.tiempoLlegada = tiempoLlegada;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public long getTiempoLlegada() {
        return tiempoLlegada;
    }
    public void setTiempoLlegada(long tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public long tiempoEsperaActual(long tiempoActual) {
        // Calculamos la diferencia en minutos
        return (tiempoActual - this.tiempoLlegada) / 60;
    }


    public void registrarCambio(String cambio) {
        this.historialCambios.push(cambio);
    }
    public String obtenerUltimaCambio() {
        return this.historialCambios.pop();
    }
}

class AreaAtencion {
    private String nombre;
    private PriorityQueue<Paciente> pacientesHeap;
    private int capacidadMaxima;

    public AreaAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.pacientesHeap = new PriorityQueue<>((p1, p2) -> {
            if (p1.getCategoria() != p2.getCategoria()){
                return Integer.compare(p1.getCategoria(), p2.getCategoria());
            }
            return Long.compare(p1.getTiempoLlegada(), p2.getTiempoLlegada());

        });
    }

    public void ingresarPaciente(Paciente p) {
        this.pacientesHeap.offer(p);
    }
    public Paciente atenderPaciente() {
        return this.pacientesHeap.poll();
    }
    public Boolean estaSaturado() {
        if (capacidadMaxima >= this.pacientesHeap.size()) {
            return false;
        }
        else  {
            return true;
        }
    }
    public List<Paciente> obtenerPacientesPorHeapSort() {
        List<Paciente> pacientesPorHeap = new ArrayList<>();
        PriorityQueue<Paciente> pacientesHeapAux = new PriorityQueue<>(this.pacientesHeap);
        while (!pacientesHeapAux.isEmpty()) {
            pacientesPorHeap.add(pacientesHeapAux.poll());
        }
        return pacientesPorHeap;
    }

}

class Hospital {
    private Map<String, Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String, AreaAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital() {
        this.pacientesTotales = new HashMap<>();
        this.colaAtencion = new PriorityQueue<>((p1, p2) -> {
            if (p1.getCategoria() != p2.getCategoria()) {
                return Integer.compare(p1.getCategoria(), p2.getCategoria());
            }
            return Long.compare(p1.getTiempoLlegada(), p2.getTiempoLlegada());
        });
        this.areasAtencion = new HashMap<>();
        this.pacientesAtendidos = new ArrayList<>();
        areasAtencion.put("SAPU", new AreaAtencion("SAPU", 45));
        areasAtencion.put("urgencia_adulto", new AreaAtencion("urgencia_adulto", 38));
        areasAtencion.put("infantil", new AreaAtencion("infantil", 10));
    }

    public void registrarPaciente(Paciente p) {
        this.pacientesTotales.put(p.getId(), p);
        this.colaAtencion.offer(p);
    }

    public void reasignarPaciente(String id, int nuevaCategoria) {
        Paciente p = pacientesTotales.get(id);
        if (p != null) {
            int antiguaCategoria = p.getCategoria();
            p.setCategoria(nuevaCategoria);
            p.registrarCambio("De C" + antiguaCategoria + " a C" + nuevaCategoria);
        } else {
            System.out.println("No existe el paciente con el ID: " + id);
        }
    }

    public Paciente atenderSiguiente() {
        Paciente p = this.colaAtencion.poll();
        if (p != null) {
            AreaAtencion area = areasAtencion.get(p.getArea());
            Paciente pacienteAnterior = area.atenderPaciente();
            if (pacienteAnterior != null) {
                pacientesAtendidos.add(pacienteAnterior);
                pacienteAnterior.registrarCambio("Paciente atendido");
                pacienteAnterior.setEstado("atendido");
            }
            p.setEstado("en_atencion");
            area.ingresarPaciente(p);
            p.registrarCambio("Paciente siendo atendido");
        }
        return p;
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {
        List<Paciente> pacientesPorCategoria = new ArrayList<>();
        for (Paciente p : pacientesTotales.values()) {
            if (p.getCategoria() == categoria) {
                pacientesPorCategoria.add(p);
            }
        }
        return pacientesPorCategoria;
    }

    public AreaAtencion obtenerArea(String nombre) {
        return areasAtencion.get(nombre);
    }

    public PriorityQueue<Paciente> getColaAtencion() {
        return this.colaAtencion;


    }
    }

    class GenerarPacientes {
        private String[] nombres = {
                "Juan", "María", "Carlos", "Ana", "José", "Laura", "Pedro", "Isabel",
                "Miguel", "Sofía", "Fernando", "Carmen", "Daniel", "Patricia", "Francisco",
                "Rosa", "Alberto", "Elena", "Ricardo", "Diana", "Gabriel", "Mónica",
                "Eduardo", "Lucía", "Pablo", "Andrea", "Roberto", "Beatriz", "Antonio", "Teresa"
        };
        private String[] apellidos = {
                "García", "Rodríguez", "López", "Martínez", "González", "Pérez", "Sánchez", "Ramírez",
                "Torres", "Flores", "Rivera", "Gómez", "Díaz", "Reyes", "Morales", "Cruz",
                "Ortiz", "Ramos", "Romero", "Álvarez", "Mendoza", "Ruiz", "Herrera", "Medina",
                "Castro", "Vargas", "Jiménez", "Silva", "Munoz", "Delgado"
        };
        private String[] areas = {"SAPU", "urgencia_adulto", "infantil"};
        private String estado = "en_espera";
        private int id = 0;
        private Random random = new Random();

        private int generarCategoria() {
            double randomNum = random.nextDouble();
            if (randomNum < 0.10) {
                return 1;
            } else if (randomNum < 0.25) {
                return 2;
            } else if (randomNum < 0.43) {
                return 3;
            } else if (randomNum < 0.70) {
                return 4;
            } else {
                return 5;
            }
        }

        public List<Paciente> generarPacientes(int cantidad) {
            List<Paciente> pacientes = new ArrayList<>();
            long tiempo = 0;
            for (int i = 0; i < cantidad; i++) {
                long tiempoLlegada = tiempo + (i * 600);
                Paciente p = new Paciente(
                        this.nombres[this.random.nextInt(this.nombres.length)],
                        this.apellidos[this.random.nextInt(this.apellidos.length)],
                        String.valueOf(this.id++),
                        this.generarCategoria(),
                        this.estado,
                        this.areas[this.random.nextInt(this.areas.length)],
                        tiempoLlegada  // Añadir el tiempoLlegada en el constructor
                );
                pacientes.add(p);
            }
            return pacientes;
        }

        public void guardarPacientesEnArchivo(List<Paciente> pacientes, String nombreArchivo) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
                for (Paciente paciente : pacientes) {
                    String linea = String.format("ID: %s, Nombre: %s %s, Categoría: C%d, Área: %s, Llegada: %d, Estado: %s",
                            paciente.getId(),
                            paciente.getNombre(),
                            paciente.getApellido(),
                            paciente.getCategoria(),
                            paciente.getArea(),
                            paciente.getTiempoLlegada(),
                            paciente.getEstado()
                    );
                    writer.write(linea);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo: " + e.getMessage());
            }
        }



        public static void main(String[] args) {
            GenerarPacientes generador = new GenerarPacientes();
            List<Paciente> pacientes = generador.generarPacientes(144); // Generar 100 pacientes de prueba
            generador.guardarPacientesEnArchivo(pacientes, "Pacientes_24h.txt");
            System.out.println("Pacientes generados y guardados en el archivo.");
        }

    }

    class SimuladorUrgencia {
        private Hospital hospital;
        private List<Paciente> pacientesEnEspera;
        private Map<Integer, Integer> contadorPorCategoria;
        private Map<Integer, List<Long>> tiemposEspera;
        private List<Paciente> pacientesFueraTiempo;
        private int pacientesAcumulados;

        // Tiempos máximos de espera por categoría (en minutos)
        private static final Map<Integer, Integer> TIEMPOS_MAXIMOS = Map.of(
                1, 0,
                2, 30,
                3, 90,
                4, 180,
                5, Integer.MAX_VALUE
        );

        public SimuladorUrgencia() {
            this.hospital = new Hospital();
            this.pacientesEnEspera = new ArrayList<>();
            this.contadorPorCategoria = new HashMap<>();
            this.tiemposEspera = new HashMap<>();
            this.pacientesFueraTiempo = new ArrayList<>();
            this.pacientesAcumulados = 0;

            // Inicializar contadores y listas de tiempos
            for (int i = 1; i <= 5; i++) {
                contadorPorCategoria.put(i, 0);
                tiemposEspera.put(i, new ArrayList<>());
            }
        }

        public void simular(int pacientesPorDia) {
            cargarPacientesDesdeArchivo("Pacientes_24h.txt");
            int pacientesProcesados = 0;
            long timestamp_inicio = 0;

            // Inicializar mapas
            Map<String, Long> ultimoTiempoAtencionPorArea = new HashMap<>();
            for (String area : new String[]{"SAPU", "urgencia_adulto", "infantil"}) {
                ultimoTiempoAtencionPorArea.put(area, timestamp_inicio);
            }

            // Simulación por minutos (24 horas = 1440 minutos)
            for (int minuto = 0; minuto < 1440; minuto++) {
                long tiempoActual = timestamp_inicio + (minuto * 60 * 1000);

                // Verificar tiempos máximos cada 5 minutos
                if (minuto % 5 == 0) {
                    verificarTiemposMaximos(tiempoActual);
                }

                // Llegada de pacientes cada 10 minutos
                if (minuto % 10 == 0 && pacientesProcesados < pacientesPorDia) {
                    if (!pacientesEnEspera.isEmpty()) {
                        Paciente nuevoPaciente = pacientesEnEspera.remove(0);
                        nuevoPaciente.setTiempoLlegada(tiempoActual);
                        hospital.registrarPaciente(nuevoPaciente);
                        pacientesProcesados++;
                    }
                }

                // Atención de pacientes cada 15 minutos
                if (minuto % 15 == 0) {
                    for (String area : ultimoTiempoAtencionPorArea.keySet()) {
                        atenderPaciente(tiempoActual, ultimoTiempoAtencionPorArea);
                    }
                }
            }

            generarReporteSimulacion();
        }



        private void atenderPaciente(long tiempoActual, Map<String, Long> ultimoTiempoAtencionPorArea) {
            Paciente paciente = hospital.atenderSiguiente();
            if (paciente != null) {
                // Obtener el último tiempo de atención para el área del paciente
                long ultimaAtencion = ultimoTiempoAtencionPorArea.get(paciente.getArea());
                // El tiempo de atención será el máximo entre el tiempo actual y la última atención + 10 minutos
                long tiempoAtencion = Math.max(tiempoActual, ultimaAtencion + (10 * 60 * 1000));
                // Actualizar el último tiempo de atención para esta área
                ultimoTiempoAtencionPorArea.put(paciente.getArea(), tiempoAtencion);

                registrarAtencion(paciente, tiempoAtencion);
            }
        }


        private void atenderPacienteUrgente(long tiempoActual, Map<String, Long> ultimoTiempoAtencionPorArea) {
            Paciente paciente = hospital.atenderSiguiente();
            if (paciente != null) {
                // Para casos urgentes, atendemos 5 minutos después del último paciente o en el tiempo actual
                long ultimaAtencion = ultimoTiempoAtencionPorArea.get(paciente.getArea());
                long tiempoAtencion = Math.max(tiempoActual, ultimaAtencion + (5 * 60 * 1000));
                ultimoTiempoAtencionPorArea.put(paciente.getArea(), tiempoAtencion);

                registrarAtencion(paciente, tiempoAtencion);
            }
        }



        private void registrarAtencion(Paciente paciente, long tiempoAtencion) {
            long tiempoEsperaMs = tiempoAtencion - paciente.getTiempoLlegada();
            // Convertimos a minutos
            long tiempoEsperaMinutos = tiempoEsperaMs / 60000;
            tiemposEspera.get(paciente.getCategoria()).add(tiempoEsperaMinutos);
            contadorPorCategoria.put(
                    paciente.getCategoria(),
                    contadorPorCategoria.get(paciente.getCategoria()) + 1
            );
            paciente.setEstado("atendido");
            System.out.println("Debug Atención detallado - Paciente: " + paciente.getId());
            System.out.println("  Categoría: " + paciente.getCategoria());
            System.out.println("  Tiempo Llegada (ms): " + paciente.getTiempoLlegada());
            System.out.println("  Tiempo Atención (ms): " + tiempoAtencion);
            System.out.println("  Tiempo Espera (ms): " + tiempoEsperaMs);
            System.out.println("  Tiempo Espera (min): " + tiempoEsperaMinutos);


        }

        private void verificarTiemposMaximos(long tiempoActual) {
            // Crear una lista temporal para mantener los pacientes que no exceden tiempo
            List<Paciente> pacientesEnEspera = new ArrayList<>();
            PriorityQueue<Paciente> cola = hospital.getColaAtencion();

            // Limpiar la cola actual y verificar cada paciente
            while (!cola.isEmpty()) {
                Paciente paciente = cola.poll();
                long tiempoEsperaMinutos = (tiempoActual - paciente.getTiempoLlegada()) / (60 * 1000);

                // Verificar si el paciente ha excedido su tiempo máximo de espera
                int tiempoMaximo = TIEMPOS_MAXIMOS.getOrDefault(paciente.getCategoria(), Integer.MAX_VALUE);

                if (tiempoEsperaMinutos > tiempoMaximo) {
                    // Si excedió el tiempo máximo, lo agregamos a la lista de pacientes fuera de tiempo
                    pacientesFueraTiempo.add(paciente);

                    // Registrar la atención con prioridad máxima
                    registrarAtencion(paciente, tiempoActual);

                    // Actualizar estadísticas
                    contadorPorCategoria.put(
                            paciente.getCategoria(),
                            contadorPorCategoria.get(paciente.getCategoria()) + 1
                    );

                    System.out.println("¡ALERTA! Paciente ID: " + paciente.getId() +
                            " excedió tiempo máximo de espera. Categoría: " +
                            paciente.getCategoria() +
                            ", Tiempo esperado: " + tiempoEsperaMinutos +
                            " minutos (máximo: " + tiempoMaximo + " minutos)");
                } else {
                    // Si no excedió el tiempo, lo mantenemos en la cola
                    pacientesEnEspera.add(paciente);
                }
            }

            // Reintegrar los pacientes que no excedieron el tiempo máximo
            for (Paciente p : pacientesEnEspera) {
                cola.offer(p);
            }
        }



        private void validarLinea(String linea) {
            if (linea == null || linea.trim().isEmpty()) {
                throw new IllegalArgumentException("La línea está vacía");
            }

            String[] partes = linea.split(", ");
            if (partes.length != 6) {
                throw new IllegalArgumentException("La línea no tiene el formato correcto. Se esperaban 6 partes, se encontraron: " + partes.length);
            }

            if (!partes[2].contains("Categoría: C")) {
                throw new IllegalArgumentException("Formato de categoría incorrecto: " + partes[2]);
            }

            // Imprimir información de depuración
            System.out.println("Procesando línea: " + linea);
            for (int i = 0; i < partes.length; i++) {
                System.out.println("Parte " + i + ": " + partes[i]);
            }
        }


        private void cargarPacientesDesdeArchivo(String archivo) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                int numeroLinea = 0;
                while ((linea = br.readLine()) != null) {
                    numeroLinea++;
                    try {
                        validarLinea(linea);
                        Paciente p = parsearLineaPaciente(linea);
                        if (p != null) {
                            pacientesEnEspera.add(p);
                        }
                    } catch (Exception e) {
                        System.err.println("Error en línea " + numeroLinea + ": " + e.getMessage());
                    }
                }
                System.out.println("Pacientes cargados exitosamente: " + pacientesEnEspera.size());
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        }


        private Paciente parsearLineaPaciente(String linea) {
            try {
                String[] partes = linea.split(", ");

                // Extraer ID
                String id = partes[0].substring(partes[0].indexOf(": ") + 2);

                // Extraer nombre completo
                String nombreCompleto = partes[1].substring(partes[1].indexOf(": ") + 2);
                String[] partesNombre = nombreCompleto.split(" ");
                String nombre = partesNombre[0];
                String apellido = partesNombre[1];

                // Extraer categoría (mejorado)
                String categoriaStr = partes[2].trim();
                int categoria = Integer.parseInt(categoriaStr.substring(
                        categoriaStr.lastIndexOf("C") + 1).trim());

                // Extraer área
                String area = partes[3].substring(partes[3].indexOf(": ") + 2);

                // Extraer tiempo de llegada
                String llegadaStr = partes[4].substring(partes[4].indexOf(": ") + 2);
                long llegada = Long.parseLong(llegadaStr);

                // Extraer estado
                String estado = partes[5].substring(partes[5].indexOf(": ") + 2);

                return new Paciente(nombre, apellido, id, categoria, estado, area, llegada);

            } catch (Exception e) {
                System.err.println("Error al parsear línea: " + linea);
                System.err.println("Detalles del error: " + e.getMessage());
                return null;
            }
        }


        private void generarReporteSimulacion() {
            System.out.println("\n=== REPORTE DE SIMULACIÓN DE URGENCIAS ===\n");

            // Pacientes atendidos por categoría
            System.out.println("Pacientes atendidos por categoría:");
            contadorPorCategoria.forEach((categoria, cantidad) ->
                    System.out.printf("Categoría %d: %d pacientes\n", categoria, cantidad)
            );

            // Tiempos promedio de espera
            System.out.println("\nTiempos promedio de espera por categoría:");
            tiemposEspera.forEach((categoria, tiempos) -> {
                if (!tiempos.isEmpty()) {
                    double promedio = tiempos.stream()
                            .mapToLong(t -> t)
                            .average()
                            .getAsDouble();
                    // Ya no necesitamos dividir por 60000 porque los tiempos ya están en minutos
                    System.out.printf("Categoría %d: %.2f minutos\n", categoria, promedio);
                } else {
                    System.out.printf("Categoría %d: 0.00 minutos\n", categoria);
                }
            });

            // Pacientes fuera de tiempo
            System.out.println("\nPacientes que excedieron tiempo máximo: " +
                    pacientesFueraTiempo.size());
            if (!pacientesFueraTiempo.isEmpty()) {
                System.out.println("\nDetalle de pacientes que excedieron tiempo máximo:");
                pacientesFueraTiempo.forEach(p ->
                        System.out.printf("ID: %s, Categoría: %d, Área: %s\n",
                                p.getId(), p.getCategoria(), p.getArea())
                );
            }
        }

        public static void main(String[] args) {
            SimuladorUrgencia simulador = new SimuladorUrgencia();
            simulador.simular(144); // 24 horas con llegadas cada 10 minutos
        }
    }



