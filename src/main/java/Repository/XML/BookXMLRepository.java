package Repository.XML;

import Domain.Book;
import Domain.Validators.BookStoreException;
import Domain.Validators.ValidatorException;
import Repository.InMemory.InMemoryRepository;
import Repository.Repository;
import Domain.Validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.*;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class BookXMLRepository extends InMemoryRepository<Long, Book> {
    private String fileName;
    private Document doc;

    public BookXMLRepository(Validator<Book> validator,String fileName){
        super(validator);
        this.fileName = fileName;
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.fileName);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        try {
            loadData();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws ParserConfigurationException, IOException, SAXException {
        List<Book> result;

        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        result = IntStream.range(0,children.getLength()).
                mapToObj(index -> children.item(index)).
                filter(node -> node instanceof Element).
                map(node -> creatBookFromElement((Element) node)).
                collect(Collectors.toList());

        result.forEach(book -> {
            book.setId(Integer.toUnsignedLong(book.hashCode()));
            super.save(book);});
    }

    @Override
    public Optional<Book> delete(Long id)throws BookStoreException {
        Optional<Book> optionalBook = super.delete(id);
        //we rewrite the file
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new BookStoreException(e.getMessage());
        }
        Element root = doc.createElement("bookstore");
        doc.appendChild(root);
        //save the new changes in the "new file"
        super.findAll().forEach(book -> {
            try {
                saveToFile(book);
            } catch (TransformerException e) {
                throw new BookStoreException(e.getMessage());
            }
        });
        return optionalBook;
    }

    private Book creatBookFromElement(Element bookElement) {
        Book book = new Book();

        String category = bookElement.getAttribute("category");
        book.setCategory(category);

        book.setTitle(getTextFromTagName(bookElement,"title"));
        book.setAuhtor(getTextFromTagName(bookElement,"author"));
        book.setYear(Integer.parseInt(getTextFromTagName(bookElement,"year")));
        book.setPrice(Float.parseFloat(getTextFromTagName(bookElement,"price")));

        return book;
    }

    private String getTextFromTagName(Element parent, String tag) {
        Node node = parent.getElementsByTagName(tag).item(0);
        return node.getTextContent();
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException{
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        try {
            saveToFile(entity);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException {
        Optional<Book> optionalBook = super.update(entity);
        //we rewrite the file
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new BookStoreException(e.getMessage());
        }
        Element root = doc.createElement("bookstore");
        doc.appendChild(root);
        //save the new changes in the "new file"
        super.findAll().forEach(book -> {
            try {
                saveToFile(book);
            } catch (TransformerException e) {
                throw new BookStoreException(e.getMessage());
            }
        });
        return optionalBook;
    }

    private void saveToFile(Book book) throws ValidatorException, TransformerException {
        Element root = doc.getDocumentElement();
        Node bookNode = bookToNode(book);
        root.appendChild(bookNode);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc),new StreamResult(new File(fileName)));
    }

    public Node bookToNode(Book book) {
        Element bookElement = doc.createElement("book");
        bookElement.setAttribute("category",book.getCategory());

        appendChildWithTextNode(bookElement,"title",book.getTitle());
        appendChildWithTextNode(bookElement,"author",book.getAuhtor());
        appendChildWithTextNode(bookElement,"year",Integer.toString(book.getYear()));
        appendChildWithTextNode(bookElement,"price",Double.toString(book.getPrice()));

        return bookElement;
    }

    private void appendChildWithTextNode(Node bookElement, String tag, String val) {
        Element elem = doc.createElement(tag);
        elem.setTextContent(val);
        bookElement.appendChild(elem);
    }
}
