package Repository.XML;

import Domain.Book;
import Domain.Client;
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

public class ClientXMLRepository extends InMemoryRepository<Long, Client> {
    private String fileName;
    private Document doc;

    public ClientXMLRepository(Validator<Client> validator,String fileName){
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
        List<Client> result;

        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        result = IntStream.range(0,children.getLength()).
                mapToObj(index -> children.item(index)).
                filter(node -> node instanceof Element).
                map(node -> creatClientFromElement((Element) node)).
                collect(Collectors.toList());

        result.forEach(client -> {
            client.setId(Integer.toUnsignedLong(client.hashCode()));
            super.save(client);});
    }

    private Client creatClientFromElement(Element clientElement) {
        Client client = new Client();

        String name = clientElement.getAttribute("name");
        client.setName(name);

        client.setAddress(getTextFromTagName(clientElement,"address"));
        client.setCnp(Long.parseLong(getTextFromTagName(clientElement,"cnp")));
        client.setId(Integer.toUnsignedLong(client.hashCode()));

        return client;
    }

    private String getTextFromTagName(Element parent, String tag) {
        Node node = parent.getElementsByTagName(tag).item(0);
        return node.getTextContent();
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException{
        Optional<Client> optional = super.save(entity);
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
    public Optional<Client> delete(Long id)throws BookStoreException {
        Optional<Client> optionalClient = super.delete(id);
        //we rewrite the file
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new BookStoreException(e.getMessage());
        }
        Element root = doc.createElement("bookstore");
        doc.appendChild(root);
        //save the new changes in the "new file"
        super.findAll().forEach(client -> {
            try {
                saveToFile(client);
            } catch (TransformerException e) {
                throw new BookStoreException(e.getMessage());
            }
        });
        return optionalClient;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        Optional<Client> optionalClient = super.update(entity);
        //we rewrite the file
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new BookStoreException(e.getMessage());
        }
        Element root = doc.createElement("bookstore");
        doc.appendChild(root);
        //save the new changes in the "new file"
        super.findAll().forEach(client -> {
            try {
                saveToFile(client);
            } catch (TransformerException e) {
                throw new BookStoreException(e.getMessage());
            }
        });
        return optionalClient;
    }


    private void saveToFile(Client client) throws ValidatorException, TransformerException {
        Element root = doc.getDocumentElement();
        Node clientNode = clientToNode(client);
        root.appendChild(clientNode);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc),new StreamResult(new File(fileName)));
    }

    private Node clientToNode(Client client) {
        Element clientElement = doc.createElement("client");
        clientElement.setAttribute("name",client.getName());

        appendChildWithTextNode(clientElement,"address",client.getAddress());
        appendChildWithTextNode(clientElement,"cnp",String.valueOf(client.getCnp()));

        return clientElement;
    }

    private void appendChildWithTextNode(Node bookElement, String tag, String val) {
        Element elem = doc.createElement(tag);
        elem.setTextContent(val);
        bookElement.appendChild(elem);
    }
}