package com.fisa.wooriarte.spacephoto.dto;

import com.fisa.wooriarte.exhibit.domain.Exhibit;
import com.fisa.wooriarte.matching.domain.Matching;
import com.fisa.wooriarte.spaceItem.domain.SpaceItem;
import com.fisa.wooriarte.spaceItem.repository.SpaceItemRepository;
import com.fisa.wooriarte.spacephoto.domain.SpacePhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class SpacePhotoDTO {

    private Long spacePhotoId;
    private Long spaceItemId;
    private String fileName;
    private String url;
    private String s3KeyName; // S3 키 값을 저장하는 필드 추가

    public SpacePhoto toEntity(SpaceItemRepository spaceItemRepository) {
        // spaceItemId를 찾아서 Optional로 받음
        Optional<SpaceItem> optionalSpaceItem = spaceItemRepository.findById(this.spaceItemId);

        // Optional에서 SpaceItem이 존재하지 않으면 예외 발생
        SpaceItem spaceItem = optionalSpaceItem.orElseThrow(() -> new IllegalArgumentException("SpaceItem not found with id: " + this.spaceItemId));
        return SpacePhoto.builder()
                .spaceItem(spaceItem)
                .fileName(this.fileName)
                .url(this.url)
                .s3KeyName(this.s3KeyName) // s3KeyName 값도 함께 설정
                .build();
    }

    public static SpacePhotoDTO fromEntity(SpacePhoto spacePhoto) {
        if (spacePhoto == null) {
            return null;
        }
        return SpacePhotoDTO.builder()
                .spacePhotoId(spacePhoto.getSpacePhotoId())
                .spaceItemId(spacePhoto.getSpaceItem().getSpaceItemId())
                .fileName(spacePhoto.getFileName())
                .url(spacePhoto.getUrl())
                .s3KeyName(spacePhoto.getS3KeyName()) // s3KeyName 값도 함께 설정
                .build();
    }
}
