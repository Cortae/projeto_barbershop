package school.sptech.apl_api_barbershop.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import school.sptech.apl_api_barbershop.dto.ClienteItemResponse;
import school.sptech.apl_api_barbershop.dto.ClienteRequest;
import school.sptech.apl_api_barbershop.dto.ClienteResponse;
import school.sptech.apl_api_barbershop.entity.Cliente;
import school.sptech.apl_api_barbershop.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteResponse cadastrar(ClienteRequest request) {

        boolean celularRepetido = repository.existsByTelefone(request.celular());

        Cliente cliente = new Cliente();

        cliente.setNome(request.nome());
        cliente.setApelido(request.apelido());
        cliente.setTelefone(request.celular());
        cliente.setNascimento(request.nascimento());

        Cliente clienteSalvo = repository.save(cliente);

        String aviso = null;

        if (celularRepetido) {
            aviso = "Este celular já está cadastrado em outro cliente.";
        }

        return converterParaResponse(clienteSalvo, aviso);
    }

    public ClienteResponse editar(Integer id, ClienteRequest request) {

        Optional<Cliente> clienteOptional = repository.findById(id);

        if (clienteOptional.isEmpty()) {
            return null;
        }

        Cliente cliente = clienteOptional.get();

        cliente.setNome(request.nome());
        cliente.setApelido(request.apelido());
        cliente.setTelefone(request.celular());
        cliente.setNascimento(request.nascimento());

        Cliente clienteAtualizado = repository.save(cliente);

        return converterParaResponse(clienteAtualizado, null);
    }

    public ClienteResponse buscar(Integer id) {

        Optional<Cliente> clienteOptional = repository.findById(id);

        if (clienteOptional.isEmpty()) {
            return null;
        }

        return converterParaResponse(clienteOptional.get(), null);
    }

    public List<ClienteItemResponse> listar(String busca) {

        Sort ordenacao = Sort.by("nome").ascending();

        List<Cliente> clientes;

        if (busca == null || busca.isBlank()) {
            clientes = repository.findAll(ordenacao);
        } else {
            clientes = repository
                    .findByNomeContainingIgnoreCaseOrTelefoneContainingIgnoreCase(
                            busca,
                            busca,
                            ordenacao
                    );
        }

        return clientes.stream()
                .map(cliente -> new ClienteItemResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getTelefone(),
                        null,
                        null
                ))
                .toList();
    }

    private ClienteResponse converterParaResponse(Cliente cliente, String aviso) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getApelido(),
                cliente.getTelefone(),
                cliente.getNascimento(),
                aviso
        );
    }
}