package model;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private int stock;
    private int idCategoria;


    public Libro(int id, String titulo, String autor, String isbn, String editorial, int stock, int idCategoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getAutor() {
        return autor;
    }

    public int getStock() {
        return stock;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return titulo + " (stock: " + stock + ")";
    }
}
