package Service;

import Domain.Client;
import Repository.Repository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, Client> repo;

    public ClientService(Repository<Long, Client> r) {this.repo = r;}

    /**
     * Saves the client in the repository, if valid.
     * @param client must not be null
     */
    public void addClient(Client client) {repo.save(client); }

    public void updateClient(Client client){
        repo.update(client);
    }

    public void deleteClient(Long id){
        repo.delete(id);
    }

    public Long findIdByCnp(Long cnp){
        Iterable<Client> clients = repo.findAll();
        return StreamSupport.stream(clients.spliterator(),false).filter(client -> client.getCnp() == cnp).map(client -> client.getId()).reduce((long) 0,Long::sum);
    }

    public Long findId(String name){
        Iterable<Client> clients = repo.findAll();
        return StreamSupport.stream(clients.spliterator(),false).filter(client -> client.getName().equals(name)).map(client -> client.getId()).reduce((long) 0,Long::sum);
    }

    /**
     * Returns a set containing all the clients from the repository.
     * @return an {@code Set<Client>}
     */
    public Set<Client> getAllClients(){
        Iterable<Client> clients = repo.findAll();
        Set<Client> filteredSet = StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
        return filteredSet;
    }

    /** Returns a set containing clients that have the given name.
     * @param name not null
     * @return an {@code Set<Client>}
     */
    public Set<Client> filterbyName(String name){
        Iterable<Client> Clients = repo.findAll();
        Set<Client> filteredSet = StreamSupport.stream(Clients.spliterator(),false).filter(Client -> Client.getName().contains(name)).collect(Collectors.toSet());
        return filteredSet;
    }

    /**
     * Returns a set containing clients that have the given address.
     * @param address not null
     * @return an {@code Set<Client>}
     */
    public Set<Client> filterbyAddress(String address){
        Iterable<Client> Clients = repo.findAll();
        Set<Client> filteredSet = StreamSupport.stream(Clients.spliterator(),false).filter(Client -> Client.getAddress().contains(address)).collect(Collectors.toSet());
        return filteredSet;
    }


    
}
