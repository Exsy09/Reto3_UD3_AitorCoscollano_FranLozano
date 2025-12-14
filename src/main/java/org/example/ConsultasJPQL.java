package org.example;


import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * ConsultasJPQL
 *
 * Clase de ejemplo que agrupa consultas JPQL sobre las entidades:
 * - Mascotas
 * - Propietarios
 * - Veterinarios
 *
 */
public class ConsultasJPQL {

    /*EntityManager:
     *  Es el principal punto de entrada para operar con JPA.
     *  Administra el contexto de persistencia (ciclo de vida de las entidades).
     *  No debe compartirse entre hilos; cada petición/unidad de trabajo debería obtener su propio EM desde el factory.
     */
    private EntityManager em;

    /* Constructor que recibe el EntityManager desde el exterior.
     *  facilita las pruebas.
     */
    public ConsultasJPQL(EntityManager em) {
        this.em = em;
    }


    // 1. LISTADO DE TODAS LAS ENTIDADES SELECCIONANDO SOLO ALGUNOS CAMPOS
    //    (EN ESTE CASO SE SELECCIONA LA ENTIDAD COMPLETA)


    /*listarMascotas
     * Devuelve la lista completa de todas las entidades Mascotas registradas.
     */
    public List<Mascotas> listarMascotas() {
        return em.createQuery(
                "SELECT m FROM Mascotas m",  // JPQL: selecciona la entidad Mascotas con alias m
                Mascotas.class               // Tipo de retorno: lista de Mascotas
        ).getResultList();                 // Ejecuta la consulta y devuelve la lista (posible lista vacía)
    }

    /* listarPropietarios
     * Devuelve todos los objetos Propietarios.
     */
    public List<Propietarios> listarPropietarios() {
        return em.createQuery(
                "SELECT p FROM Propietarios p",
                Propietarios.class
        ).getResultList();
    }

    /*listarVeterinarios
     * Devuelve todos los objetos Veterinarios.
     */
    public List<Veterinarios> listarVeterinarios() {
        return em.createQuery(
                "SELECT v FROM Veterinarios v",
                Veterinarios.class
        ).getResultList();
    }


    // 2. LISTADO FILTRADO POR ALGÚN CRITERIO (WHERE)


    /*mascotasPorEspecie
     * Devuelve todas las Mascotas cuya propiedad 'especie' es exactamente igual al parámetro.
     */
    public List<Mascotas> mascotasPorEspecie(String especie) {
        return em.createQuery(
                        "SELECT m FROM Mascotas m WHERE m.especie = :especie",
                        Mascotas.class
                )
                .setParameter("especie", especie) // binding seguro del parámetro
                .getResultList();                 // devuelve lista (vacía si no hay coincidencias)
    }

    /*mascotasPorSexo
     *Filtra por el campo 'sexo' de Mascotas.
     */
    public List<Mascotas> mascotasPorSexo(String sexo) {
        return em.createQuery(
                        "SELECT m FROM Mascotas m WHERE m.sexo = :sexo",
                        Mascotas.class
                )
                .setParameter("sexo", sexo)
                .getResultList();
    }


    // 3. LISTADOS ORDENADOS POR ALGÚN CRITERIO


    /*mascotasOrdenadasPorNombre
     * Devuelve mascotas ordenadas por el atributo 'nombre' en orden ascendente.
     */
    public List<Mascotas> mascotasOrdenadasPorNombre() {
        return em.createQuery(
                "SELECT m FROM Mascotas m ORDER BY m.nombre ASC",
                Mascotas.class
        ).getResultList();
    }

    /*mascotasOrdenadasPorNacimiento
     *  Ordena por fecha de nacimiento (nacimiento ascendente).
     */
    public List<Mascotas> mascotasOrdenadasPorNacimiento() {
        return em.createQuery(
                "SELECT m FROM Mascotas m ORDER BY m.nacimiento ASC",
                Mascotas.class
        ).getResultList();
    }


    // 4. BÚSQUEDAS EXACTAS Y APROXIMADAS


    /* buscarMascotaPorNombreExacto
     *  Búsqueda por igualdad exacta del nombre.
     */
    public List<Mascotas> buscarMascotaPorNombreExacto(String nombre) {
        return em.createQuery(
                        "SELECT m FROM Mascotas m WHERE m.nombre = :nombre",
                        Mascotas.class
                )
                .setParameter("nombre", nombre)
                .getResultList();
    }

    /* buscarMascotasPorNombreAproximado
     *  Búsqueda parcial usando LIKE,  % delante y detrás del patrón.
     */
    public List<Mascotas> buscarMascotasPorNombreAproximado(String patron) {
        return em.createQuery(
                        "SELECT m FROM Mascotas m WHERE m.nombre LIKE :patron",
                        Mascotas.class
                )
                .setParameter("patron", "%" + patron + "%") // patrón para buscar dentro del texto
                .getResultList();
    }


    // 5. RANGO NUMÉRICO (RANGO DE FECHAS EN ESTE CASO)


    /*mascotasNacidasEntre
     *  Devuelve mascotas nacidas entre dos fechas inclusivas.
     */
    public List<Mascotas> mascotasNacidasEntre(LocalDate inicio, LocalDate fin) {
        return em.createQuery(
                        "SELECT m FROM Mascotas m WHERE m.nacimiento BETWEEN :ini AND :fin",
                        Mascotas.class
                )
                .setParameter("ini", inicio)
                .setParameter("fin", fin)
                .getResultList();
    }

    // 6. FUNCIONES AGREGADAS (count, min, max)


    /*contarMascotas
     *  Devuelve el número total de mascotas (COUNT).
     */
    public Long contarMascotas() {
        return em.createQuery(
                "SELECT COUNT(m) FROM Mascotas m",
                Long.class
        ).getSingleResult();
    }

    /* contarMascotasPorEspecie
     *  Cuenta solo las mascotas que pertenezcan a la especie indicada.
     */
    public Long contarMascotasPorEspecie(String especie) {
        return em.createQuery(
                        "SELECT COUNT(m) FROM Mascotas m WHERE m.especie = :especie",
                        Long.class
                )
                .setParameter("especie", especie)
                .getSingleResult();
    }

    /*fechaNacimientoMasAntigua
     * Devuelve la fecha mínima de nacimiento (el registro con la fecha más antigua).
     */
    public LocalDate fechaNacimientoMasAntigua() {
        return em.createQuery(
                "SELECT MIN(m.nacimiento) FROM Mascotas m",
                LocalDate.class
        ).getSingleResult();
    }

    // 7. JOIN con relaciones

    /*mascotasConPropietarios
     *  JOIN entre Mascotas y Propietarios para filtrar sólo las mascotas que tienen un propietario asociado.
     */
    public List<Mascotas> mascotasConPropietarios() {
        return em.createQuery(
                "SELECT m FROM Mascotas m JOIN m.propietario p",
                Mascotas.class
        ).getResultList();
    }

    /*mascotasConVeterinarios
     *JOIN ManyToMany entre Mascotas y Veterinarios.
     * Devuelve mascotas que tengan al menos un veterinario asociado.
     */
    public List<Mascotas> mascotasConVeterinarios() {
        return em.createQuery(
                "SELECT m FROM Mascotas m JOIN m.veterinarios v",
                Mascotas.class
        ).getResultList();
    }

    /*propietariosConMascotas
     * Obtiene la lista de propietarios activos que poseen al menos una mascota registrada.
     */
    public List<Propietarios> propietariosConMascotas() {
        return em.createQuery(
                "SELECT p FROM Propietarios p JOIN p.mascotas m",
                Propietarios.class
        ).getResultList();
    }
    //8 consultas mas elaboradas he echo 5 no se si tenemos que hacer mas o  no pone numero minimo ni maximo

    /* 1 Mascotas sin veterinarios asignados.
     * Útil para detectar registros huérfanos en una relación ManyToMany.
     */
    public List<Mascotas> mascotasSinVeterinario() {
        return em.createQuery(
                        "SELECT m FROM Mascotas m WHERE m.veterinarios IS EMPTY",
                        Mascotas.class)
                .getResultList();
    }

    /*2 Propietarios con más de X mascotas.
     */
    public List<Propietarios> propietariosConMasDeXMascotas(int x) {
        return em.createQuery(
                        "SELECT p FROM Propietarios p WHERE SIZE(p.mascotas) > :cantidad",
                        Propietarios.class)
                .setParameter("cantidad", x)
                .getResultList();
    }

    /*3 Mascotas atendidas por un veterinario específico (por nombre).
     */
    public List<Mascotas> mascotasAtendidasPorVeterinario(String nombreVet) {
        return em.createQuery(
                        "SELECT m FROM Mascotas m JOIN m.veterinarios v WHERE v.nombre = :nom",
                        Mascotas.class)
                .setParameter("nom", nombreVet)
                .getResultList();
    }

    /* 4 Promedio de edad (en años) de todas las mascotas.
     *se usa función YEAR en JPQL (Hibernate lo soporta).
     */
    public Double promedioEdadMascotas() {
        return em.createQuery(
                        "SELECT AVG(YEAR(CURRENT_DATE) - YEAR(m.nacimiento)) FROM Mascotas m",
                        Double.class)
                .getSingleResult();
    }

    /*5 Mascotas que comparten propietario con otra mascota concreta.
     */
    public List<Mascotas> mascotasConMismoPropietarioQue(String nombreMascota) {
        return em.createQuery(
                        "SELECT m2 FROM Mascotas m1 JOIN m1.propietario p JOIN p.mascotas m2 " +
                                "WHERE m1.nombre = :nom AND m2.nombre <> :nom",
                        Mascotas.class)
                .setParameter("nom", nombreMascota)
                .getResultList();
    }
}

