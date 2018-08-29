package rmafia.phraseditector;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VideoRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    public Video findByTitle(String title){
        TypedQuery<Video> namedQuery = entityManager.createNamedQuery("find_by_title", Video.class);
        return namedQuery.setParameter("val", title).getSingleResult();
    }

    public Video findByVideoId(String videoId){
        TypedQuery<Video> query = entityManager.createQuery("select v from Video v where v.videoId = :val", Video.class);
        return query.setParameter("val", videoId).getSingleResult();
    }
}
