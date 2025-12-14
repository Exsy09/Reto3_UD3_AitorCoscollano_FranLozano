package org.example;



import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;

public class PruebasConsultas {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadMascotas");
        EntityManager em = emf.createEntityManager();

        ConsultasJPQL consultas = new ConsultasJPQL(em);

        // 1. Listado de todas las entidades
        System.out.println("=== TODAS LAS MASCOTAS ===");
        System.out.println(consultas.listarMascotas());

        System.out.println("\n=== TODOS LOS PROPIETARIOS ===");
        System.out.println(consultas.listarPropietarios());

        System.out.println("\n=== TODOS LOS VETERINARIOS ===");
        System.out.println(consultas.listarVeterinarios());

        // 2. Listado filtrado por algún criterio (WHERE)
        System.out.println("\n=== MASCOTAS POR ESPECIE (Perro) ===");
        System.out.println(consultas.mascotasPorEspecie("Perro"));

        System.out.println("\n=== MASCOTAS POR SEXO (Macho) ===");
        System.out.println(consultas.mascotasPorSexo("Macho"));

        // 3. Listados ordenados
        System.out.println("\n=== MASCOTAS ORDENADAS POR NOMBRE ===");
        System.out.println(consultas.mascotasOrdenadasPorNombre());

        System.out.println("\n=== MASCOTAS ORDENADAS POR NACIMIENTO ===");
        System.out.println(consultas.mascotasOrdenadasPorNacimiento());

        // 4. Búsqueda exacta y aproximada
        System.out.println("\n=== BUSCAR MASCOTA POR NOMBRE EXACTO (Miki) ===");
        System.out.println(consultas.buscarMascotaPorNombreExacto("Miki"));

        System.out.println("\n=== BUSCAR MASCOTAS POR NOMBRE APROXIMADO ('Mi') ===");
        System.out.println(consultas.buscarMascotasPorNombreAproximado("Mi"));

        // 5. Rango de fechas
        System.out.println("\n=== MASCOTAS NACIDAS ENTRE 2005-01-01 Y 2010-12-31 ===");
        System.out.println(consultas.mascotasNacidasEntre(LocalDate.of(2008,12,20), LocalDate.of(2014,8,1)));
        // 6. Funciones agregadas
        System.out.println("\n=== TOTAL DE MASCOTAS ===");
        System.out.println(consultas.contarMascotas());

        System.out.println("\n=== TOTAL DE MASCOTAS POR ESPECIE (Perro) ===");
        System.out.println(consultas.contarMascotasPorEspecie("Perro"));

        System.out.println("\n=== FECHA DE NACIMIENTO MÁS ANTIGUA ===");
        System.out.println(consultas.fechaNacimientoMasAntigua());

        // 7. JOINs
        System.out.println("\n=== MASCOTAS CON PROPIETARIOS ===");
        System.out.println(consultas.mascotasConPropietarios());

        System.out.println("\n=== MASCOTAS CON VETERINARIOS ===");
        System.out.println(consultas.mascotasConVeterinarios());

        System.out.println("\n=== PROPIETARIOS CON MASCOTAS ===");
        System.out.println(consultas.propietariosConMascotas());

        //8 consultas mas elaboradas

        System.out.println("\n=== MASCOTAS SIN VETERINARIO ===");
        System.out.println(consultas.mascotasSinVeterinario());

        System.out.println("\n=== PROPIETARIOS CON MÁS DE 1 MASCOTA ===");
        System.out.println(consultas.propietariosConMasDeXMascotas(1));

        System.out.println("\n=== MASCOTAS ATENDIDAS POR EL VETERINARIO 'Dra.Ramirez' ===");
        System.out.println(consultas.mascotasAtendidasPorVeterinario("Dra.Ramirez"));

        System.out.println("\n=== PROMEDIO DE EDAD DE TODAS LAS MASCOTAS ===");
        System.out.println(consultas.promedioEdadMascotas());

        System.out.println("\n=== MASCOTAS QUE COMPARTEN PROPIETARIO CON 'Lucho' ===");
        System.out.println(consultas.mascotasConMismoPropietarioQue("Lucho"));


        em.close();
        emf.close();
    }
}
