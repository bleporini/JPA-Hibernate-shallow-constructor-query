package org.blep.poc.hcq;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.hibernate.transform.Transformers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author blep
 *         Date: 12/03/12
 *         Time: 07:10
 */
public class HotelTest {

    @Rule
    public JunitTimer junitTimer = new JunitTimer();

    private EntityManager em = Persistence.createEntityManagerFactory("HotelsPU").createEntityManager();

    @Before
    public void setUp() throws Exception {
        em.getTransaction().begin();
    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    /**
     * Basic test just for checking 
     * @throws Exception
     */
    
    @Test
    public void testName() throws Exception {
        List<Hotel> hotels = em.createQuery("select h from Hotel h", Hotel.class).getResultList();
        assertEquals(100, hotels.size());
        for (Hotel hotel : hotels) {
//            System.out.println("hotel.getName() = " + hotel.getName());
            hotel.getName();
        }
    }

    /**
     * Standard select with a constructor JPQL query ==> n+1 statements
     * @throws Exception
     */

    @Test
    public void testConstructor100SqlWithJpql() throws Exception {
        TypedQuery<HotelDto> query = em.createQuery("select  new org.blep.poc.hcq.HotelDto(h) from Hotel h", HotelDto.class);

        List<HotelDto> resultList = query.getResultList();
        for (HotelDto dto : resultList) {
            dto.getName();
        }
    }

    /**
     * Criteria select query with constructor ==> useless as Hibernate translates Criteria into JPQL ==> n+1 statements
     * @throws Exception
     */
    @Test
    public void testConstructorCriteria100Sql() throws Exception {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<HotelDto> cq = criteriaBuilder.createQuery(HotelDto.class);
        Root<Hotel> from = cq.from(Hotel.class);
        cq.select(criteriaBuilder.construct(HotelDto.class, from));

        TypedQuery<HotelDto> query = em.createQuery(cq);

        for (HotelDto dto : (List<HotelDto>) query.getResultList()) {
            dto.getName();
        }

    }

    /**
     * This is a workaround but it implies to reveal Hibernate API, so the code is not "only JPA" any longer...
     *
     * @throws Exception
     */
    @Test

    public void testUnwrapHibernateQuery() throws Exception {
        long delay = System.currentTimeMillis();
        @SuppressWarnings("JpaQlInspection") Query query = em.createQuery("select h as hotel from Hotel h");
        org.hibernate.Query unwrapped = query.unwrap(org.hibernate.Query.class);
        unwrapped.setResultTransformer(Transformers.aliasToBean(HotelDto.class));

        for (HotelDto dto : (List<HotelDto>) query.getResultList()) {
            //System.out.println("dto.getState() = " + dto.getState());
            dto.getName();
        }

    }

    /**
     * Using a collection implementation dedicated to the Dto usage.
     *
     * @throws Exception
     */
    @Test
    public void testCollectionDelegate() throws Exception {

        HotelDtoCollection hotels = new HotelDtoCollection(em.createQuery("select h from Hotel h", Hotel.class).getResultList());
        assertEquals(100, hotels.size());
        for (HotelDto hotel : hotels) {
//            System.out.println("hotel.getName() = " + hotel.getName());
            hotel.getName();
        }

    }

    /**
     *  Using Guava collection to translate the result list to another list.
     * @throws Exception
     */
    @Test
    public void testGuavaStyle() throws Exception {

        List<Hotel> resultList = em.createQuery("select h from Hotel h", Hotel.class).getResultList();

        List<HotelDto> dtos = Lists.transform(resultList, new Function<Hotel, HotelDto>() {
            public HotelDto apply(@Nullable Hotel input) {
                return new HotelDto(input);
            }
        });

        for (HotelDto dto : dtos) {
            dto.getName();

        }
    }
}
