package Repository.XML;

import Domain.Book;
import Domain.Client;
import Domain.Purchase;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PurchaseXMLRepository extends InMemoryRepository<Long, Purchase> {
    private String fileName;
    private Document doc;

    public PurchaseXMLRepository(Validator<Purchase> validator,String fileName){
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
        List<Purchase> result;

        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        result = IntStream.range(0,children.getLength()).
                mapToObj(index -> children.item(index)).
                filter(node -> node instanceof Element).
                map(node -> creatPurchaseFromElement((Element) node)).
                collect(Collectors.toList());

        result.forEach(purchase -> {
            purchase.setId(Integer.toUnsignedLong(purchase.hashCode()));
            super.save(purchase);});
    }

    private Purchase creatPurchaseFromElement(Element purchaseElement) {
        Purchase purchase = new Purchase();

        String nr = purchaseElement.getAttribute("purchaseNumber");
        purchase.setPurchaseNumber(Integer.parseInt(nr));

        purchase.setBookID(getTextFromTagName(purchaseElement,"bookID"));
        purchase.setClientID(Long.parseLong(getTextFromTagName(purchaseElement,"clientID")));
        purchase.setQuantity(Integer.parseInt(getTextFromTagName(purchaseElement,"quantity")));
        purchase.setId(Integer.toUnsignedLong(purchase.hashCode()));

        return purchase;
    }

    private String getTextFromTagName(Element parent, String tag) {
        Node node = parent.getElementsByTagName(tag).item(0);
        return node.getTextContent();
    }

    @Override
    public Optional<Purchase> save(Purchase entity) throws ValidatorException{
        Optional<Purchase> optional = super.save(entity);
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
    public Optional<Purchase> delete(Long id)throws BookStoreException {
        Optional<Purchase> optionalPurchase = super.delete(id);
        //we rewrite the file
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new BookStoreException(e.getMessage());
        }
        Element root = doc.createElement("bookstore");
        doc.appendChild(root);
        //save the new changes in the "new file"
        super.findAll().forEach(purchase -> {
            try {
                saveToFile(purchase);
            } catch (TransformerException e) {
                throw new BookStoreException(e.getMessage());
            }
        });
        return optionalPurchase;
    }

    @Override
    public Optional<Purchase> update(Purchase entity) throws ValidatorException {
        Optional<Purchase> optionalPurchase = super.update(entity);
        //we rewrite the file
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new BookStoreException(e.getMessage());
        }
        Element root = doc.createElement("bookstore");
        doc.appendChild(root);
        //save the new changes in the "new file"
        super.findAll().forEach(purchase -> {
            try {
                saveToFile(purchase);
            } catch (TransformerException e) {
                throw new BookStoreException(e.getMessage());
            }
        });
        return optionalPurchase;
    }


    private void saveToFile(Purchase purchase) throws ValidatorException, TransformerException {
        Element root = doc.getDocumentElement();
        Node purchaseNode = purchaseToNode(purchase);
        root.appendChild(purchaseNode);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc),new StreamResult(new File(fileName)));
    }

    private Node purchaseToNode(Purchase purchase) {
        Element purchaseElement = doc.createElement("purchase");
        purchaseElement.setAttribute("purchaseNumber",Integer.toString(purchase.getPurchaseNumber()));

        appendChildWithTextNode(purchaseElement,"bookID",purchase.getBookID());
        appendChildWithTextNode(purchaseElement,"clientID",String.valueOf(purchase.getClientID()));
        appendChildWithTextNode(purchaseElement,"quantity",String.valueOf(purchase.getQuantity()));

        return purchaseElement;
    }

    private void appendChildWithTextNode(Node bookElement, String tag, String val) {
        Element elem = doc.createElement(tag);
        elem.setTextContent(val);
        bookElement.appendChild(elem);
    }
}