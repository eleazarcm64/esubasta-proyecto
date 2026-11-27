package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import modelo.Articulo;
import modelo.Usuario;
import servicio.ArticuloServicio;
import servicio.PujaServicio;
import servicio.UsuarioServicio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApiServer {
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    private static final UsuarioServicio usuarioServicio = new UsuarioServicio();
    private static final ArticuloServicio articuloServicio = new ArticuloServicio();
    private static final PujaServicio pujaServicio = new PujaServicio();

    public static void iniciar(int puerto) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);

        // 1. Usuarios: Registro y Login
        server.createContext("/api/usuarios/registro", exchange -> manejar(exchange, () -> {
            if (!"POST".equals(exchange.getRequestMethod())) return metodoNoPermitido();
            Usuario u = gson.fromJson(leerBody(exchange), Usuario.class);
            Usuario creado = usuarioServicio.registrar(u);
            creado.setPassword(null); // No exponer contraseña
            return respuesta(201, gson.toJson(creado));
        }));

        server.createContext("/api/usuarios/login", exchange -> manejar(exchange, () -> {
            if (!"POST".equals(exchange.getRequestMethod())) return metodoNoPermitido();
            Map<?, ?> body = gson.fromJson(leerBody(exchange), Map.class);
            Usuario u = usuarioServicio.login((String) body.get("email"), (String) body.get("password"));
            u.setPassword(null);
            return respuesta(200, gson.toJson(u));
        }));

        server.createContext("/api/usuarios", exchange -> manejar(exchange, () -> {
            if (!"GET".equals(exchange.getRequestMethod())) return metodoNoPermitido();
            var lista = usuarioServicio.listarTodos();
            lista.forEach(u -> u.setPassword(null));
            return respuesta(200, gson.toJson(lista));
        }));

        // 2. Artículos / Subastas Activas y Publicación
        server.createContext("/api/articulos/activos", exchange -> manejar(exchange, () -> {
            if (!"GET".equals(exchange.getRequestMethod())) return metodoNoPermitido();
            return respuesta(200, gson.toJson(articuloServicio.listarSubastasActivas()));
        }));

        server.createContext("/api/categorias", exchange -> manejar(exchange, () -> {
            if (!"GET".equals(exchange.getRequestMethod())) return metodoNoPermitido();
            return respuesta(200, gson.toJson(articuloServicio.listarCategorias()));
        }));

        server.createContext("/api/articulos", exchange -> manejar(exchange, () -> {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();

            if ("GET".equals(method)) {
                if (query != null && query.startsWith("titulo=")) {
                    String titulo = query.substring("titulo=".length());
                    return respuesta(200, gson.toJson(articuloServicio.buscarPorTitulo(titulo)));
                }
                return respuesta(200, gson.toJson(articuloServicio.listarSubastasActivas()));
            }

            if ("POST".equals(method)) {
                Map<?, ?> body = gson.fromJson(leerBody(exchange), Map.class);
                Articulo art = new Articulo();
                art.setTitulo((String) body.get("titulo"));
                art.setDescripcion((String) body.get("descripcion"));
                art.setPrecio_inicial(((Number) body.get("precio_inicial")).doubleValue());
                int idVendedor = ((Number) body.get("id_vendedor")).intValue();
                int idCategoria = ((Number) body.get("id_categoria")).intValue();
                Articulo guardado = articuloServicio.publicar(art, idVendedor, idCategoria);
                return respuesta(201, gson.toJson(guardado));
            }
            return metodoNoPermitido();
        }));

        // 3. Pujas: Realizar oferta y consultar historial
        server.createContext("/api/pujas", exchange -> manejar(exchange, () -> {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();

            if ("GET".equals(method) && query != null && query.startsWith("id_articulo=")) {
                int idArt = Integer.parseInt(query.substring("id_articulo=".length()));
                return respuesta(200, gson.toJson(pujaServicio.historialPorArticulo(idArt)));
            }

            if ("POST".equals(method)) {
                Map<?, ?> body = gson.fromJson(leerBody(exchange), Map.class);
                int idArticulo = ((Number) body.get("id_articulo")).intValue();
                int idComprador = ((Number) body.get("id_comprador")).intValue();
                double monto = ((Number) body.get("monto")).doubleValue();
                return respuesta(201, gson.toJson(pujaServicio.realizarPuja(idArticulo, idComprador, monto)));
            }
            return metodoNoPermitido();
        }));

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor API E-Subasta corriendo en http://localhost:" + puerto);
    }

    // --- Utilidades internas HTTP y CORS ---
    @FunctionalInterface
    interface Accion { RespuestaHttp ejecutar() throws Exception; }
    record RespuestaHttp(int codigo, String json) {}

    private static void manejar(HttpExchange exchange, Accion accion) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        RespuestaHttp res;
        try {
            res = accion.ejecutar();
        } catch (IllegalArgumentException e) {
            res = respuesta(400, gson.toJson(Map.of("error", e.getMessage())));
        } catch (Exception e) {
            e.printStackTrace();
            res = respuesta(500, gson.toJson(Map.of("error", "Error interno: " + e.getMessage())));
        }

        byte[] bytes = res.json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(res.codigo, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String leerBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static RespuestaHttp respuesta(int codigo, String json) {
        return new RespuestaHttp(codigo, json);
    }

    private static RespuestaHttp metodoNoPermitido() {
        return new RespuestaHttp(405, "{\"error\":\"Método HTTP no permitido\"}");
    }
}