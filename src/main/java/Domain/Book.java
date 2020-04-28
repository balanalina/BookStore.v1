package Domain;
import Domain.BaseEntity;


public class Book extends BaseEntity<Long>{
    //private long id;
    private String title;
    private double price;
    private String auhtor;
    private String category;
    private int year;

    public Book(){}

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @param title
     *              must not be null
     * @param author
     *              must not be null
     * @param price
     *              must not be null
     */
    public Book(String title, String author, double price,String category,int year){
        this.title = title;
        this.auhtor = author;
        this.price = price;
        this.category = category;
        this.year = year;
    }

    /**
     * Returns the title of the book entity.
     * @return an {@code String}
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Returns the author of the book entity.
     * @return an {@code String}
     */
    public String getAuhtor(){
        return this.auhtor;
    }

    /**
     * Returns the price of the book entity.
     * @return an {@code Double}
     */
    public double getPrice(){
        return this.price;
    }

    /**
     * Sets a new value for the title of the book entity.
     * @param title
     *              new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets a new value for the price of the book entity.
     * @param price
     *              new price
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * Sets a new value for the author of the book entity.
     * @param author
     *              new author
     */
    public void setAuhtor(String author){
        this.auhtor = author;
    }
    @Override
    public String toString(){
        return "Title: "+this.title+
                "\nAuthor: "+this.auhtor
                +"\nPrice: "+Double.toString(this.price)+
                "\nCategory: " + this.getCategory() +
                "\nYear: " + Integer.toString(this.getYear())
                +"\n";
    }


    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + auhtor.hashCode();
        result = 31* result + Double.hashCode(price);
        return result;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        //if(id!=book.id) return false;
        if (price != book.price) return false;
        if (!title.equals(book.title)) return false;
        return auhtor.equals(book.auhtor);

    }
    }
