package ee.helmes.backend.dto;

public record SectorResponse(
        Integer id,
        String name,
        Integer parentId,
        Integer sortOrder
) {
}