import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public Paciente(String nombre, String apellido, String id, int categoria, String estado, String area) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.estado = estado;
        this.area = area;
        this.historialCambios = new Stack<>();
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

    public long tiempoEsperaActual() {
        return System.currentTimeMillis() - this.tiempoLlegada;
    }
    public void registrarCambio(String cambio) {
        this.historialCambios.push(cambio);
    }
    public String obtenerUltimaCambio() {
        return this.historialCambios.peek();
    }
}

class AreasAtencion{
    private String nombre;
    private PriorityQueue<Paciente> pacientesHeap;
    private int capacidadMaxima;

    public AreasAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.pacientesHeap = new PriorityQueue<>((p1, p2) -> {
            if (p1.tiempoEsperaActual() != p2.tiempoEsperaActual()){
                return Long.compare(p1.tiempoEsperaActual(), p2.tiempoEsperaActual());
            }
            return Integer.compare(p1.getCategoria(), p2.getCategoria());
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
        while (!pacientesHeap.isEmpty()) {
            pacientesPorHeap.add(this.pacientesHeap.poll());
        }
        return pacientesPorHeap;
    }

}

class Hospital {
    private Map<String, Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String, AreasAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital() {
        this.pacientesTotales = new HashMap<>();
        this.colaAtencion = new PriorityQueue<>((p1, p2) -> {
            if (p1.getCategoria() != p2.getCategoria()){
                return Integer.compare(p1.getCategoria(), p2.getCategoria());
            }
            return Long.compare(p1.tiempoEsperaActual(), p2.tiempoEsperaActual());
        });
        this.areasAtencion = new HashMap<>();
        this.pacientesAtendidos = new ArrayList<>();
    }

    public void registrarPaciente(Paciente p) {
        this.pacientesTotales.put(p.getId(), p);
    }
    /*public void reasignarPaciente(String id, int nuevaCategoria) {

    }
    public Paciente atenderSiguiente() {

    }
    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {

    }
    public AreasAtencion obtenerArea(String nombre) {

    }*/

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
    private String[] areas = {
            "SAPU", "urgencia_adulto", "infantil"};
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
            Paciente p = new Paciente(
                    this.nombres[this.random.nextInt(this.nombres.length)],
                    this.apellidos[this.random.nextInt(this.apellidos.length)],
                    String.valueOf(this.id++),
                    this.generarCategoria(),
                    this.estado,
                    this.areas[this.random.nextInt(this.areas.length)]
            );
            long tiempoLlegada = tiempo + (i * 600);
            p.setTiempoLlegada(tiempoLlegada);
            pacientes.add(p);
        }
        return pacientes;
    }

    public void guardarPacientesEnArchivo(List<Paciente> pacientes, String nombreArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Paciente paciente : pacientes) {
                writer.write(
                        String.format(
                                "ID: %s, Nombre: %s %s, Categoría: C%d, Llegada: %d, Estado: %s",
                                paciente.getId(),
                                paciente.getNombre(),
                                paciente.getApellido(),
                                paciente.getCategoria(),
                                paciente.getTiempoLlegada(),
                                paciente.getEstado()
                        )
                );
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