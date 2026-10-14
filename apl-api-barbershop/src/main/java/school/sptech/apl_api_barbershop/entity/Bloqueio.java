package school.sptech.apl_api_barbershop.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueio")
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloqueio")
    private Long idBloqueio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fim", nullable = false)
    private LocalDateTime fim;

    @Column(name = "motivo", length = 100)
    private String motivo;

    public Long getIdBloqueio() {
        return idBloqueio;
    }

    public void setIdBloqueio(Long idBloqueio) {
        this.idBloqueio = idBloqueio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
