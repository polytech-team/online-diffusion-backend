package team.polytech.online.diffusion.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import team.polytech.online.diffusion.entity.ImageEntity;
import team.polytech.online.diffusion.entity.User;

@Repository
public interface ImageRepository extends CrudRepository<ImageEntity, Long>, PagingAndSortingRepository<ImageEntity, Long> {

    Page<ImageEntity> findAllByPublicity(ImageEntity.Publicity publicity, Pageable pageable);

    Page<ImageEntity> findAllByUserAndPublicityNot(User user, ImageEntity.Publicity publicity, Pageable pageable);

}
