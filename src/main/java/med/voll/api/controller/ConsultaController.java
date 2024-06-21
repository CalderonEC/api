package med.voll.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@ResponseBody
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultaService service;

    @PostMapping
    @Operation(
            summary = "Registrar una nueva consulta",
            description = "Crea un nuevo registro de consulta en la base de datos.",
            tags = {"consulta","post"})
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datos) throws ValidacionDeIntegridad {

        var response = service.agendar(datos);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "Cancelar una consulta",
            description = "Marca una consulta como cancelada en la base de datos.")
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DatosCancelamientoConsulta datos) {
        service.cancelar(datos);
        return ResponseEntity.noContent().build();
    }
}
