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
        this.tiempoLlegada = System.currentTimeMillis();
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

    public long tiempoEsperaActual() {
        return System.currentTimeMillis() - this.tiempoLlegada;
    }
    public void registrarCambio(String cambio) {
        this.historialCambios.push(cambio);
    }
    public String obtenerUltimaCambio() {
        return this.historialCambios.pop();
    }
}

class AreasAtencion{
    private String nombre;
    private PriorityQueue<Paciente> pacientesHeap;
    private int capacidadMaxima;

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

    }

}

class Hospital {
    private Map<String, Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String, AreasAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital() {
        this.pacientesTotales = new HashMap<>();
        this.colaAtencion = new PriorityQueue<>();
        this.areasAtencion = new HashMap<>();
        this.pacientesAtendidos = new ArrayList<>();
    }

    public void registrarPaciente(Paciente p) {
        this.pacientesTotales.put(p.getId(), p);
    }
    public void reasignarPaciente(String id, int nuevaCategoria) {

    }
    public Paciente atenderSiguiente() {

    }
    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {

    }
    public AreasAtencion obtenerArea(String nombre) {

    }

}