package com.GestionSNKR.SnearksSource; // Ajusta el paquete si lo moviste a config

import com.GestionSNKR.SnearksSource.model.Blogs;
import com.GestionSNKR.SnearksSource.model.Cliente;
import com.GestionSNKR.SnearksSource.model.Producto;
import com.GestionSNKR.SnearksSource.repository.BlogsRepository;
import com.GestionSNKR.SnearksSource.repository.ClienteRepository;
import com.GestionSNKR.SnearksSource.repository.ProductoRepository;
import com.GestionSNKR.SnearksSource.service.BoletaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class ConfiguracionInicial {

    private static Map<String, Integer> inventarioBase() {
        return Map.of(
                "35", 1,
                "36", 2,
                "37", 4,
                "38", 5,
                "39", 3,
                "40", 4,
                "41", 2,
                "42", 1
        );
    }

    @Bean
    CommandLineRunner iniciarBaseDeDatos(ProductoRepository productoRepository, BlogsRepository blogsRepository, ClienteRepository clienteRepository) {
        return args -> {

            if (clienteRepository.count() == 0) {
                System.out.println("Creando usuario por defecto...");
                Cliente clienteDemo = new Cliente();
                clienteDemo.setNombreCompleto("Sebastián");
                clienteDemo.setEmail("minipokersbdos@gmail.com");
                clienteDemo.setContrasena("minipoker");
                clienteDemo.setNumero("912345678");
                clienteDemo.setDireccion("Calle Falsa 123");
                clienteDemo.setRegion("Metropolitana");
                clienteDemo.setComuna("Providencia");
                clienteRepository.save(clienteDemo);
                System.out.println("Usuario por defecto creado con éxito.");
            }

            // ==========================================
            // PRECARGA DE ZAPATILLAS
            // ==========================================
            if (productoRepository.count() == 0) {
                System.out.println("Iniciando la precarga de zapatillas en la base de datos H2...");

                List<Producto> zapatillas = List.of(
                        new Producto(null, 149000, "Air Jordan Retro 1", "Air Jordan 1 Retro en cuero de Jordan con puntera redonda, suela de goma, logo Nike, al tobillo.", "img/re1.png", "Jordan", inventarioBase()),
                        new Producto(null, 165000, "Air Jordan Retro 3", "Air Jordan 3 Retro OG black/cement en cuero, logo en relieve en la lengüeta, cierre con agujetas en la parte delantera.", "img/re3.png", "Jordan", inventarioBase()),
                        new Producto(null, 175000, "Air Jordan Retro 4", "Jumpman característico, puntera redonda, cierre con agujetas en la parte delantera, parche del logo en la lengüeta.", "img/re4.png", "Jordan", inventarioBase()),
                        new Producto(null, 189000, "Air Jordan 5 “UNC”", "Azul claro, Jumpman característico, cierre con agujetas en la parte delantera, lengüeta oversize , plantilla con logo y suela de goma.", "img/re5.png", "Jordan", inventarioBase()),
                        new Producto(null, 199000, "Air Jordan 11 Retro", "Air Jordan 11 Retro de Jordan con puntera redonda, cierre con cordones en la parte delantera, plantilla con logo, acolchado en los tobillos y suela dentada de goma.", "img/re11.png", "Jordan", inventarioBase()),
                        new Producto(null, 210000, "Air Jordan 12 Retro", "Air Jordan 12 Retro en cuero de color rojo, blanco y negro de Jordan con puntera de almendra, agujetas en la parte delantera, logo en relieve en la lengüeta, plantilla con logo, acolchado en los tobillos, lengüeta en la parte posterior, logo en el contrafuerte y suela de goma.", "img/re12.png", "Jordan", inventarioBase()),
                        new Producto(null, 230000, "Air Jordan 13 Retro", "Air Jordan 13 Retro en goma de Jordan, en color axul con blanco, lengüeta oversize.", "img/re13.png", "Jordan", inventarioBase()),
                        new Producto(null, 250000, "Air Jordan 1 Low Travis Scott", "Air Jordan 1 Low OG SP de Jordan x Travis Scott, cuero, logo Air Jordan Wings característico, Colaboración exclusiva con Travis Scott", "img/tra1.png", "Jordan", inventarioBase()),
                        new Producto(null, 250000000, "Maxi Black Cat", "Colaboración exclusiva con Travis Scott", "img/maxi.jpg", "Viejas Desnudas", inventarioBase()),
                        new Producto(null, 149000, "Mercurial Vapor 16", "¿La velocidad es tu obsesión? También lo es para las mayores estrellas del fútbol. Por eso creamos estos tacos Elite con una unidad Air Zoom mejorada de 3/4 de largo. Entregan una sensación de propulsión que te ayuda a despegar desde la línea de fondo con máxima rapidez.", "img/imgsp/m12.png", "Nike Sports", inventarioBase()),
                        new Producto(null, 165000, "Phantom 6 Low Elite", "Phantom 6 Elite con el revolucionario Gripknit para potenciar tu precisión. La textura adherente mejora el contacto del pie con la pelota. ", "img/imgsp/m2.png", "Nike Sports", inventarioBase()),
                        new Producto(null, 175000, "Mercurial Vapor 17 Elite", "Diseñados para impulsar sprints rápidos, la suave capellada NikeSkin de los Vapor 17 Academy te prepara para el despegue. ", "img/imgsp/m31.png", "Nike Sports", inventarioBase()),
                        new Producto(null, 189000, "Tiempo Maestro Academy", "Con los Tiempo Maestro Academy, tendrás el control de cada toque de la pelota. Confeccionados con cuero FlyTouch, tienen una textura suave que te ofrece un control total de la pelota.", "img/imgsp/ti.png", "Nike Sports", inventarioBase()),
                        new Producto(null, 199000, "Mercurial Superfly 11 Elite SE", "Hecho para atacar en un parpadeo, el Superfly 11 Elite libera una velocidad explosiva al combinar la amortiguación Air Zoom con la espuma ZoomX, ultrarreactiva.", "img/imgsp/e2.png", "Nike Sports", inventarioBase()),
                        new Producto(null, 210000, "Phantom 6 Low Elite \"Alexia Putellas\"", "Desde el trono de Alexia, lo ve todo. Para asegurarse de que pueda dominar la cancha con precisión, las Phantom 6 Elite están equipadas con la revolucionaria tecnología Gripknit y una placa de tracción Cyclone 360.", "img/imgsp/f1 (1).png", "Nike Sports", inventarioBase()),
                        new Producto(null, 230000, "Mercurial Superfly 11 Elite", "Los Superfly 11 Elite son increíblemente rápidos. Desatan una velocidad explosiva al combinar la amortiguación elástica Air Zoom con la espuma ultraresponsiva ZoomX.", "img/imgsp/g1.png", "Nike Sports", inventarioBase()),
                        new Producto(null, 250000, "United Tiempo Maestro Academy", "Te permiten dominar cada toque. Su cuero sintético FlyTouch, con textura suave, te da total control del balón para desestabilizar cualquier defensa.", "img/imgsp/h1.png", "Nike Sports", inventarioBase()),
                                new Producto(null, 122990, "Air Force 1 '07", "Diseño icónico de corte bajo con silueta de cuero y amortiguación Nike Air para confort diario.", "img/urban/a1 (3).png", "Nike Urbano", Map.of("39", 8, "40", 12, "41", 10, "42", 6)),
                                new Producto(null, 57990, "SB Dunk Low Pro ISO", "Edición de skateboarding con acolchado acolchado en la lengüeta y unidad Zoom Air en el talón.", "img/urban/e1.png", "Nike Urbano", Map.of("38", 4, "39", 6, "40", 8, "41", 5)),
                                new Producto(null, 86990, "Dunk Low Retro", "Ícono del baloncesto retro con revestimientos de cuero duradero y combinación de colores clásicos.", "img/urban/b3.png", "Nike Urbano", Map.of("39", 5, "40", 9, "41", 11, "42", 7)),
                                new Producto(null, 69990, "Court Vision Low", "Estilo ochentero inspirado en las canchas con materiales sintéticos sustentables y perfil bajo.", "img/urban/court.png", "Nike Urbano", Map.of("39", 10, "40", 15, "41", 12, "42", 8)),
                                new Producto(null, 139990, "Air Max 90", "Clásico atemporal con suela con diseño gofre, revestimientos cosidos y la emblemática cámara de aire.", "img/urban/90.png", "Nike Urbano", Map.of("38", 3, "39", 7, "40", 10, "41", 9)),
                                new Producto(null, 99990, "Blazer Mid '77 Vintage", "Estilo retro con acabado en cuero y detalles en gamuza, entresuela vintage y cuello acolchado alto.", "img/urban/blazer.png", "Nike Urbano", Map.of("39", 6, "40", 11, "41", 8, "42", 7)),
                                new Producto(null, 149990, "Air Max 1", "El modelo pionero de la cápsula de aire visible con una mezcla elegante de malla y gamuza.", "img/urban/air.png", "Nike Urbano", Map.of("38", 5, "39", 8, "40", 10, "41", 6)),
                                new Producto(null, 84990, "Air Max Plus Tn", "Impulsa tu actitud con el Nike Air Max Plus, una experiencia Air optimizada que ofrece una estabilidad premium y una amortiguación increíble", "img/urban/tn.png", "Nike Urbano", Map.of("39", 8, "40", 12, "41", 10, "42", 6))
                );

                productoRepository.saveAll(zapatillas);
                System.out.println("¡Zapatillas precargadas con éxito!");
            }




            // ==========================================
            // PRECARGA DE BLOGS
            // ==========================================
            if (blogsRepository.count() == 0) {
                System.out.println("Iniciando la precarga de blogs en la base de datos H2...");

                List<Blogs> posts = List.of(
                        new Blogs(null,
                                "La historia de las Air Jordan 1",
                                "Descubre cómo un par de zapatillas cambió el mundo del baloncesto.",
                                "En 1985, Nike y Michael Jordan se unieron para crear una de las siluetas más icónicas de la historia. A pesar de las multas de la NBA por romper el código de vestimenta, las Jordan 1 se convirtieron en un fenómeno cultural que perdura hasta el día de hoy. Lo que comenzó como un diseño enfocado en rendimiento terminó transformándose en un símbolo de estilo, identidad y cultura sneaker. Desde el clásico colorway Chicago hasta versiones premium y colaboraciones exclusivas, la Jordan 1 sigue siendo un referente para coleccionistas, deportistas y amantes del streetwear.",
                                "https://static.nike.com/a/images/f_auto,cs_srgb/w_1920,c_limit/89c121fc-3d07-4de0-aef6-bcc9c2764a2c/air-jordan-1-2022-lost-and-found-chicago-la-inspiraci%C3%B3n-detr%C3%A1s-del-dise%C3%B1o.jpg"),

                        new Blogs(null,
                                "Cómo cuidar tus sneakers",
                                "Tips y trucos para mantener tus zapatillas como nuevas.",
                                "Mantener tus zapatillas limpias es fundamental para que duren más tiempo. Recuerda usar cepillos de cerdas suaves, productos de limpieza específicos para cada material (cuero, gamuza, tela) y nunca, jamás, meterlas a la lavadora o secadora. Además, es importante dejar que se sequen a temperatura ambiente, lejos de fuentes de calor directo, y protegerlas con un spray impermeabilizante si las usas con frecuencia. Un cuidado constante no solo prolonga su vida útil, también preserva el color, la estructura y el valor de cada par.",
                                "https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?auto=format&fit=crop&w=600&q=80"),

                        new Blogs(null,
                                "Lanzamientos más esperados de este año",
                                "El calendario definitivo para los amantes del streetwear.",
                                "Este año promete colaboraciones increíbles. Desde nuevas ediciones de Nike Dunk y Air Max hasta proyectos especiales con artistas y atletas de renombre. Para muchos aficionados, el verdadero reto es no perderse los drops más exclusivos, porque cada lanzamiento trae consigo una mezcla de historia, diseño y comunidad sneaker. Si te gusta seguir cada noticia, es clave estar atento a fechas de lanzamiento, rumores y stock en tiendas oficiales y boutiques selectas.",
                                "https://cdn.shopify.com/s/files/1/0631/6644/7787/files/Screenshot_106_2048x2048.png?v=1781704062"),

                        new Blogs(null,
                                "Las mejores siluetas para vestir casual",
                                "Encuentra el equilibrio perfecto entre comodidad y estilo.",
                                "Cuando se trata de outfits casuales, las zapatillas son la pieza clave que define toda la estética. Siluetas como las Nike Air Force 1, New Balance 550 y Adidas Samba han conquistado la moda contemporánea porque combinan sencillez, versatilidad y una cultura visual muy fuerte. Están pensadas para acompañar jeans, cargos, joggers y looks más elegantes sin perder la comodidad del día a día.",
                                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "Cómo elegir el tamaño ideal para tus sneakers",
                                "Evita errores comunes al comprar online.",
                                "Elegir la talla correcta puede ser la diferencia entre una compra memorable y una experiencia frustrante. Cada marca tiene una forma distinta de calzar, y además hay diferencias según el material, la suela y la estructura del empeine. Una buena práctica es medir tu pie al final del día, leer la guía de tallas del fabricante y, si es posible, comparar con un par que ya tengas. Además, algunos modelos requieren un ajuste más holgado o más ceñido según tu estilo de uso.",
                                "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "Air Max: la evolución del estilo urbano",
                                "Descubre por qué esta silueta sigue siendo un clásico.",
                                "La Nike Air Max se convirtió en una referencia del sneaker culture por su innovación visual y su comodidad. Con su cámara de aire visible, no solo aportó un avance técnico, sino también una identidad estética única que se volvió imposible de ignorar. A lo largo de los años, la familia Air Max ha explorado colorways, materiales y colaboraciones que la mantienen vigente entre amantes del streetwear y atletas casuales.",
                                "https://images.unsplash.com/photo-1608231387042-66d1773070a5?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "Sneakers premium: ¿merece la pena invertir en una buena pareja?",
                                "La calidad, el confort y la duración tienen valor real.",
                                "Aunque un par más económico puede cumplir su función, las zapatillas premium suelen ofrecer mejor construcción, materiales más resistentes y una experiencia de uso más cuidada. Esto se nota en la suela, la amortiguación, el encaje y la apariencia general. Si buscas invertir en una silueta que te acompañe por años, vale la pena analizar no solo el precio, sino también la calidad del diseño y la comodidad a largo plazo.",
                                "https://images.unsplash.com/photo-1552346154-21d32810aba3?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "El auge de la cultura sneaker en Latinoamérica",
                                "Cómo la moda urbana se volvió un fenómeno global.",
                                "La cultura sneaker ha crecido de manera exponencial en toda Latinoamérica, donde el interés por marcas, ediciones exclusivas y estilo personal se consolida cada año. Lo que antes era un nicho se convirtió en una comunidad activa, con tiendas, resellers y coleccionistas que siguen cada lanzamiento con atención. Este fenómeno refleja una conexión profunda entre moda, música, deporte y expresión personal, y demuestra que las zapatillas dejan de ser un simple calzado para convertirse en una forma de identidad.",
                                "https://images.unsplash.com/photo-1511556820780-d912e42b4980?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "Nike vs Adidas: ¿cuál marca encaja mejor contigo?",
                                "Un análisis simple para elegir según tu estilo y necesidades.",
                                "Elegir entre Nike y Adidas depende tanto del estilo como del propósito del calzado. Nike suele destacar por su innovación técnica, sus cortes mínimos y su enfoque en rendimiento y streetwear. Adidas, por su parte, ha ganado fuerza por su estética retro, sus colaboraciones y la versatilidad de su catálogo. Lo importante es identificar qué te importa más: comodidad, diseño, performance o moda urbana. Conocer tus prioridades hace más fácil elegir la marca ideal.",
                                "https://images.unsplash.com/photo-1605348532760-6753d2c43329?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "Qué tienen en común los sneakers de colección",
                                "Más allá del valor: diseño, historia y rareza.",
                                "Los sneakers de colección no se venden solo por su valor monetario; se valoran por su historia, su cronología y la forma en que representan una época. Muchas siluetas exclusivas se vuelven objetos de deseo por la combinación de materiales, campañas, ediciones limitadas y su relación con la cultura pop. Para los coleccionistas, cada par es una pieza única que cuenta una historia y refleja la evolución del estilo dentro del mercado sneaker.",
                                "https://images.unsplash.com/photo-1560769629-975ec94e6a86?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "La importancia de la suela en la comodidad",
                                "No subestimes este detalle cuando compras zapatillas.",
                                "La suela es una de las partes más importantes para la comodidad diaria. Determina la amortiguación, la tracción, la estabilidad y la sensación que tendrás al caminar o correr. Algunos modelos priorizan capas más flexibles y suaves, mientras que otros trabajan con unidades más altas y resistentes para un mejor rendimiento. Entender la diferencia entre suelas y tecnologías de amortiguación puede ayudarte a elegir un modelo que se adapte mejor a tu estilo de vida y uso.",
                                "https://images.unsplash.com/photo-1543508282-6319a3e2621f?auto=format&fit=crop&w=1200&q=80"),

                        new Blogs(null,
                                "Cómo armar un outfit con zapatillas statement",
                                "Haz que tus sneakers sean el centro de atención sin perder equilibrio.",
                                "Cuando eliges una sneaker más audaz, el resto del outfit debe ser más sobrio para que el zapato tome protagonismo sin saturar la imagen. Una buena estrategia es combinar tonos neutros, prendas con cortes sencillos y accesorios minimalistas. De esta manera, la silueta destacada puede verse como un punto focal claro y al mismo tiempo mantener una estética equilibrada y moderna. La clave está en controlar el resto del look para que la protagonista sea la calzado.",
                                "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?auto=format&fit=crop&w=1200&q=80")
                );

                blogsRepository.saveAll(posts);
                System.out.println("¡Blogs precargados con éxito!");
            }
        };
    }

    @Bean
    CommandLineRunner enviarBoletaAlIniciar(BoletaService boletaService) {
        return args -> {
            System.out.println("Enviando boleta de prueba al iniciar...");
            boletaService.enviarBoletaDePrueba(1L);
        };
    }

}