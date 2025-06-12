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
    private Map<Integer, Integer> pacientesPorCategoria;
    private Map<Integer, List<Long>> tiemposEsperaPorCategoria;
    private List<Paciente> pacientesFueraTiempo;

    // Tiempos máximos de espera por categoría (en minutos)
    private static final Map<Integer, Integer> TIEMPOS_MAXIMOS = Map.of(
            1, 0,      // Inmediato
            2, 30,     // 30 minutos
            3, 90,     // 90 minutos
            4, 180,    // 180 minutos
            5, Integer.MAX_VALUE  // Sin límite específico
    );

    public SimuladorUrgencia() {
        this.hospital = new Hospital();
        this.pacientesEnEspera = new ArrayList<>();
        this.pacientesPorCategoria = new HashMap<>();
        this.tiemposEsperaPorCategoria = new HashMap<>();
        this.pacientesFueraTiempo = new ArrayList<>();

        // Inicializar contadores y listas de tiempos
        for (int i = 1; i <= 5; i++) {
            pacientesPorCategoria.put(i, 0);
            tiemposEsperaPorCategoria.put(i, new ArrayList<>());
        }

        // Cargar pacientes desde el archivo generado
        cargarPacientesDesdeArchivo("Pacientes_24h.txt");
    }

    public void simular(int pacientesPorDia) {
        long tiempoSimulacion = 24 * 60; // 24 horas en minutos
        long tiempoActual = 0;
        int pacientesNuevos = 0;

        System.out.println("Iniciando simulación de 24 horas de urgencias hospitalarias...");

        while (tiempoActual < tiempoSimulacion) {
            // Cada 10 minutos llega un nuevo paciente
            if (tiempoActual % 10 == 0 && !pacientesEnEspera.isEmpty()) {
                Paciente nuevoPaciente = pacientesEnEspera.remove(0);
                // Almacenamos el tiempo de llegada en minutos
                nuevoPaciente.setTiempoLlegada(tiempoActual);
                hospital.registrarPaciente(nuevoPaciente);
                pacientesNuevos++;
                System.out.println("Minuto " + tiempoActual + ": Llegó paciente ID: " + nuevoPaciente.getId() +
                        ", Categoría: C" + nuevoPaciente.getCategoria());
            }

            // Cada 15 minutos se atiende a un paciente
            if (tiempoActual % 15 == 0 && tiempoActual > 0) {
                atenderPaciente(tiempoActual);
            }

            // Si se acumulan 3 nuevos pacientes, se atienden 2 de forma inmediata
            if (pacientesNuevos >= 3) {
                System.out.println("Minuto " + tiempoActual + ": Se acumularon 3 pacientes, atendiendo 2 inmediatamente");
                atenderPaciente(tiempoActual);
                atenderPaciente(tiempoActual);
                pacientesNuevos = 0;
            }

            // Verificar pacientes que excedieron tiempo máximo de espera
            verificarTiemposMaximos(tiempoActual);

            tiempoActual++;
        }

        // Generar reporte final
        generarReporteSimulacion();
    }

    private void atenderPaciente(long tiempoActual) {
        PriorityQueue<Paciente> colaAtencion = hospital.getColaAtencion();
        if (!colaAtencion.isEmpty()) {
            Paciente paciente = colaAtencion.poll();

            // Registrar tiempo de espera (en minutos)
            long tiempoEspera = tiempoActual - paciente.getTiempoLlegada();
            tiemposEsperaPorCategoria.get(paciente.getCategoria()).add(tiempoEspera);

            // Actualizar contadores
            pacientesPorCategoria.put(
                    paciente.getCategoria(),
                    pacientesPorCategoria.get(paciente.getCategoria()) + 1
            );

            // Actualizar estado del paciente
            paciente.setEstado("atendido");

            System.out.println("Minuto " + tiempoActual + ": Atendido paciente ID: " + paciente.getId() +
                    ", Categoría: C" + paciente.getCategoria() +
                    ", Tiempo de espera: " + tiempoEspera + " minutos");
        }
    }

    private void verificarTiemposMaximos(long tiempoActual) {
        // Creamos una copia temporal de la cola para no modificarla mientras iteramos
        PriorityQueue<Paciente> colaOriginal = hospital.getColaAtencion();
        PriorityQueue<Paciente> nuevaCola = new PriorityQueue<>((p1, p2) -> {
            if (p1.getCategoria() != p2.getCategoria()) {
                return Integer.compare(p1.getCategoria(), p2.getCategoria());
            }
            return Long.compare(p1.getTiempoLlegada(), p2.getTiempoLlegada());
        });

        List<Paciente> pacientesExcedidos = new ArrayList<>();

        // Revisar cada paciente para ver si excedió su tiempo máximo de espera
        while (!colaOriginal.isEmpty()) {
            Paciente paciente = colaOriginal.poll();
            long tiempoEspera = tiempoActual - paciente.getTiempoLlegada();
            int tiempoMaximo = TIEMPOS_MAXIMOS.get(paciente.getCategoria());

            if (tiempoEspera > tiempoMaximo && tiempoMaximo > 0) {
                // Excedió tiempo máximo, lo atendemos inmediatamente
                pacientesExcedidos.add(paciente);
                System.out.println("¡ALERTA! Minuto " + tiempoActual + ": Paciente ID: " + paciente.getId() +
                        " (Categoría C" + paciente.getCategoria() + ") excedió tiempo máximo de espera: " +
                        tiempoEspera + " minutos (máximo: " + tiempoMaximo + " minutos)");
            } else {
                // No excedió tiempo, lo devolvemos a la cola
                nuevaCola.offer(paciente);
            }
        }

        // Atender inmediatamente los pacientes que excedieron su tiempo
        for (Paciente paciente : pacientesExcedidos) {
            tiemposEsperaPorCategoria.get(paciente.getCategoria()).add(tiempoActual - paciente.getTiempoLlegada());
            pacientesPorCategoria.put(
                    paciente.getCategoria(),
                    pacientesPorCategoria.get(paciente.getCategoria()) + 1
            );
            paciente.setEstado("atendido");
            pacientesFueraTiempo.add(paciente);
        }

        // Restaurar la cola con los pacientes que no excedieron tiempo
        hospital.getColaAtencion().clear();
        while (!nuevaCola.isEmpty()) {
            hospital.getColaAtencion().offer(nuevaCola.poll());
        }
    }

    private void cargarPacientesDesdeArchivo(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    Paciente paciente = parsearLineaPaciente(linea);
                    if (paciente != null) {
                        // Convertir el tiempo de llegada a minutos (si está en segundos)
                        long tiempoEnMinutos = paciente.getTiempoLlegada() / 60;
                        paciente.setTiempoLlegada(tiempoEnMinutos);
                        pacientesEnEspera.add(paciente);
                    }
                } catch (Exception e) {
                    System.err.println("Error al parsear línea: " + linea);
                    System.err.println("Detalles: " + e.getMessage());
                }
            }
            System.out.println("Se cargaron " + pacientesEnEspera.size() + " pacientes del archivo");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private Paciente parsearLineaPaciente(String linea) {
        try {
            String[] partes = linea.split(", ");

            // Extraer ID
            String id = partes[0].substring(partes[0].indexOf(": ") + 2);

            // Extraer nombre y apellido
            String nombreCompleto = partes[1].substring(partes[1].indexOf(": ") + 2);
            String[] partesNombre = nombreCompleto.split(" ");
            String nombre = partesNombre[0];
            String apellido = partesNombre[1];

            // Extraer categoría
            String categoriaStr = partes[2].substring(partes[2].lastIndexOf("C") + 1);
            int categoria = Integer.parseInt(categoriaStr);

            // Extraer área
            String area = partes[3].substring(partes[3].indexOf(": ") + 2);

            // Extraer tiempo de llegada
            String tiempoLlegadaStr = partes[4].substring(partes[4].indexOf(": ") + 2);
            long tiempoLlegada = Long.parseLong(tiempoLlegadaStr);

            // Extraer estado
            String estado = partes[5].substring(partes[5].indexOf(": ") + 2);

            return new Paciente(nombre, apellido, id, categoria, estado, area, tiempoLlegada);
        } catch (Exception e) {
            System.err.println("Error analizando línea: " + linea);
            System.err.println("Mensaje de error: " + e.getMessage());
            return null;
        }
    }

    private void generarReporteSimulacion() {
        System.out.println("\n=== REPORTE FINAL DE SIMULACIÓN ===");

        // 1. Cantidad de pacientes por categoría atendidos en total
        System.out.println("\nCantidad de pacientes atendidos por categoría:");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Categoría C" + i + ": " + pacientesPorCategoria.get(i) + " pacientes");
        }

        // 2. Promedios de tiempo de espera por categoría
        System.out.println("\nTiempo promedio de espera por categoría:");
        for (int i = 1; i <= 5; i++) {
            List<Long> tiempos = tiemposEsperaPorCategoria.get(i);
            double promedio = tiempos.isEmpty() ? 0 :
                    tiempos.stream().mapToLong(Long::longValue).average().orElse(0);
            System.out.printf("Categoría C%d: %.2f minutos\n", i, promedio);
        }

        // 3. Lista de pacientes que excedieron el tiempo máximo de espera
        System.out.println("\nPacientes que excedieron el tiempo máximo de espera: " + pacientesFueraTiempo.size());
        for (Paciente p : pacientesFueraTiempo) {
            // El tiempo de espera es la diferencia entre su tiempo de llegada y el tiempo actual
            long tiempoEspera = p.tiempoEsperaActual(p.getTiempoLlegada() + TIEMPOS_MAXIMOS.get(p.getCategoria()));
            System.out.printf("ID: %s, Categoría: C%d, Tiempo de espera: %d minutos\n",
                    p.getId(), p.getCategoria(), tiempoEspera);
        }
    }

    public static void main(String[] args) {
        SimuladorUrgencia simulador = new SimuladorUrgencia();
        simulador.simular(144); // 144 pacientes para 24 horas (1 cada 10 minutos)
    }
}



