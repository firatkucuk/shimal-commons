
package com.github.shimal.commons.dao;

import java.io.Serializable;
import java.util.List;
import javax.sql.DataSource;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;



public class GenericDAOImpl extends HibernateDaoSupport implements GenericDAO {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private DataSource     dataSource;
    private SessionFactory sessionFactory;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public long count(String query) {

        return (Long) getSession().createQuery(query).uniqueResult();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    // dao.count(User.class, "item.active != 1");
    @Override
    @Transactional(readOnly = true)
    public <T> long count(Class<T> c, String where) {

        return count(c, where, (Object[]) null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    // dao.count(User.class, "item.username = ?", "admin");
    @Override
    @Transactional(readOnly = true)
    public <T> long count(Class<T> c, String where, Object value) {

        return count(c, where, new Object[] { value });
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    // dao.count(User.class, "item.username = ? and item.active = ?", "admin", true);
    @Override
    @Transactional(readOnly = true)
    public <T> long count(Class<T> c, String where, Object... values) {

        String     whereClause = ((where == null) || where.isEmpty()) ? "" : ("where " + where);
        List<Long> list        = getHibernateTemplate().find("select count(*) from " + c.getSimpleName() + " item " +
                    whereClause, values);

        return list.isEmpty() ? 0 : list.get(0);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    // dao.countAll(User.class);
    @Override
    @Transactional(readOnly = true)
    public <T> long countAll(Class<T> c) {

        return count(c, null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = false)
    public <T> void delete(T item) {

        Session session = sessionFactory.getCurrentSession();
        session.delete(item);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> find(Class<T> c, String query) {

        return find(c, query, (Object[]) null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> find(Class<T> c, String query, Object value) {

        return find(c, query, new Object[] { value });
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> find(Class<T> c, String query, Object... values) {

        return getHibernateTemplate().find(query, values);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findAll(Class<T> c) {

        return getHibernateTemplate().find("from " + c.getSimpleName());
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByCriteria(DetachedCriteria criterion) {

        return getHibernateTemplate().findByCriteria(criterion);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByCriteria(DetachedCriteria criterion, int firstResult, int maxResults) {

        return getHibernateTemplate().findByCriteria(criterion, firstResult, maxResults);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByExample(T exampleEntity, int firstResult, int maxResults) {

        return (List<T>) getHibernateTemplate().findByExample(exampleEntity, firstResult, maxResults);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByNamedParam(String queryString, String[] paramNames, Object[] values) {

        return (List<T>) getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> T first(Class<T> c) {

        return first(c, null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> T first(Class<T> c, String query) {

        return first(c, query, (Object[]) null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> T first(Class<T> c, String query, Object value) {

        return first(c, query, new Object[] { value });
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> T first(Class<T> c, String query, Object... values) {

        HibernateTemplate ht = new HibernateTemplate(getSessionFactory());
        ht.setMaxResults(1);

        List<T> items = ht.find(query, values);

        return items != null && !items.isEmpty() ? items.get(0) : null;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> T get(Class<T> c, Serializable id) {

        return getHibernateTemplate().get(c, id);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> Criterion getCriteria(Class<T> c) {

        return (Criterion) getHibernateTemplate().getSessionFactory().openSession().createCriteria(c);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public JdbcTemplate newJdbcTemplate() {

        return new JdbcTemplate(dataSource);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = false)
    public <T> void persist(T item) throws DataAccessException {

        getHibernateTemplate().persist(item);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = false)
    public <T> T save(T item) {

        // Aynı anda iki session açıksa saveOrUpdate hataya düşüyor merge düzgün çalışıyor.
        getHibernateTemplate().merge(item);

        return item;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> select(Class<T> c, String hql) {

        return (List<T>) getSession().createQuery(hql).list();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> select(Class<T> c, String hql, int firstResult, int maxResults) {

        Query query = getSession().createQuery(hql);
        query.setFirstResult(firstResult).setMaxResults(maxResults).setFetchSize(maxResults).setTimeout(30);

        return (List<T>) query.list();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setDataSource(DataSource dataSource) {

        this.dataSource = dataSource;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> take(Class<T> c, int maxResultCount) {

        return take(c, maxResultCount, null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> take(Class<T> c, int maxResultCount, String query) {

        return take(c, maxResultCount, query, (Object[]) null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> take(Class<T> c, int maxResultCount, String query, Object value) {

        return take(c, maxResultCount, query, new Object[] { value });
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> take(Class<T> c, int maxResultCount, String query, Object... values) {

        HibernateTemplate ht = new HibernateTemplate(getSessionFactory());
        ht.setMaxResults(maxResultCount);

        return ht.find(query, values);
    }
}
