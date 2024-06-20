package com.example.eksamen24backend.api;

import com.example.eksamen24backend.dto.ResultDto;
import com.example.eksamen24backend.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping
    public ResultDto addResult(@RequestBody ResultDto request) {
        return resultService.addResult(request);
    }

    @GetMapping
    public List<ResultDto> getAllResults() {
        return resultService.getAllResults();
    }

    @GetMapping("/{id}")
    public ResultDto getResultById(@PathVariable Integer id) {
        return resultService.getResultById(id);
    }

    @PutMapping("/{id}")
    public ResultDto editResult(@PathVariable Integer id, @RequestBody ResultDto request) {
        return resultService.editResult(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResult(@PathVariable Integer id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public List<ResultDto> filterResults(
            @RequestParam(required = false) String discipline,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageGroup) {
        return resultService.filterResults(discipline, gender, ageGroup);
    }
}
