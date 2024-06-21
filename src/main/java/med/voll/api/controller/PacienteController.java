package med.voll.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Operation(summary = "Registrar un nuevo paciente",
            description = "Crea un nuevo registro de paciente en la base de datos.")
    @Transactional
        public ResponseEntity registrar(@RequestBody DatosRegistroPaciente datos, UriComponentsBuilder uriComponentsBuilder) {
            var paciente = new Paciente(datos);
            repository.save(new Paciente(datos));

            var uri = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
            return ResponseEntity.created(uri).body(new DatosDetalladoPaciente(paciente));
        }

    @GetMapping
    @Operation(summary = "Obtener todos los pacientes activos",
            description = "Retorna una lista con todos los pacientes que tienen el atributo 'activo' como verdadero.")
    public ResponseEntity<Page<DatosListaPaciente>> listar(@PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion) {
        var page = repository.findByActivoTrue(paginacion).map(DatosListaPaciente::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Operation(summary = "Actualizar los datos de un paciente",
            description = "Modifica los datos de un paciente existente en la base de datos.")
    @Transactional
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizacionPaciente datos) {
        var paciente = repository.getReferenceById(datos.id());
        paciente.actualizarDatosPaciente(datos);
        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un paciente",
            description = "Marca un paciente como inactivo en la base de datos.")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.eliminar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un paciente por ID",
            description = "Retorna los datos de un paciente específico a partir de su ID.")
    public ResponseEntity detallar(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
    }


}
