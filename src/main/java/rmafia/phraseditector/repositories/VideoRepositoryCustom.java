package rmafia.phraseditector.repositories;

import org.springframework.stereotype.Repository;
import rmafia.phraseditector.entities.Video;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VideoRepositoryCustom {
    private static final String Q_FIND_BY_TITLE = "select v from Video v where v.title = :val";
    private static final String Q_FIND_BY_VIDEOID = "select v from Video v where v.videoId = :val";

    @PersistenceContext
    EntityManager entityManager;

    public Video findByTitle(String title){
        TypedQuery<Video> namedQuery = entityManager.createNamedQuery(Q_FIND_BY_TITLE, Video.class);
        return namedQuery.setParameter("val", title).getSingleResult();
    }

    public Video findByVideoId(String videoId){
        TypedQuery<Video> query = entityManager.createQuery(Q_FIND_BY_VIDEOID, Video.class);
        return query.setParameter("val", videoId).getSingleResult();
    }
}
