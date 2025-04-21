// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: TauSecure::backend::bw.co.roguesystems.tau::application::ApplicationService
 * STEREOTYPE:  Service
 */
package bw.co.roguesystems.tau.application;

import bw.co.roguesystems.tau.PropertySearchOrder;
import bw.co.roguesystems.tau.SearchObject;
import bw.co.roguesystems.tau.SortOrderFactory;
import bw.co.roguesystems.tau.access.type.AccessPointType;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.roguesystems.tau.application.ApplicationService
 */
@Service("applicationService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class ApplicationServiceImpl
    extends ApplicationServiceBase
{

    private final ApplicationRepository applicationRepository;
    public ApplicationServiceImpl(
        ApplicationDao applicationDao,
        ApplicationRepository applicationRepository,
        MessageSource messageSource
    ) {
        
        super(
            applicationDao,
            applicationRepository,
            messageSource
        );
        
        this.applicationRepository = applicationRepository;
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#findById(String)
     */
    @Override
    protected ApplicationDTO handleFindById(String id)
        throws Exception
    {

        return applicationDao.toApplicationDTO(applicationRepository.findById(id).orElse(null));
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#save(ApplicationDTO)
     */
    @Override
    protected ApplicationDTO handleSave(ApplicationDTO application)
        throws Exception
    {

        Application entity = applicationDao.applicationDTOToEntity(application);
        applicationDao.create(entity);
        return applicationDao.toApplicationDTO(entity);
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        Application entity = applicationRepository.findById(id).orElse(null);
        if (entity != null)
        {
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#getAll()
     */
    @Override
    protected Collection<ApplicationDTO> handleGetAll()
        throws Exception
    {

        Collection<Application> entities = applicationRepository.findAll();

        Collection<ApplicationDTO> dtos = applicationDao.toApplicationDTOCollection(entities);
        return dtos;
    }

    private Specification<Application> getSpecification(String criteria)
    {

        Specification<Application> spec = null;

        if (StringUtils.isNotBlank(criteria)) {

            spec = (root, query, cb) -> {
                return cb.or(
                        cb.like(cb.lower(root.get("code")), "%" + criteria.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("name")), "%" + criteria.toLowerCase() + "%"));
            };
        }

        return spec;
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#search(String, Set<PropertySearchOrder>)
     */
    @Override
    protected Collection<ApplicationDTO> handleSearch(String criteria, Set<PropertySearchOrder> orderings)
        throws Exception
    {

        Specification<Application> spec = getSpecification(criteria);

        Sort sort = SortOrderFactory.createSortOrder(orderings);

        Collection<Application> entities = applicationRepository.findAll(spec, sort);
        Collection<ApplicationDTO> dtos = applicationDao.toApplicationDTOCollection(entities);
        return dtos;
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#getAll(Integer, Integer)
     */
    @Override
    protected Page<ApplicationDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Application> page = applicationRepository.findAll(pageRequest);

        Page<ApplicationDTO> dtos = page.map(applicationDao::toApplicationDTO);
        return dtos;
    }

    /**
     * @see bw.co.roguesystems.tau.application.ApplicationService#search(SearchObject<String>)
     */
    @Override
    protected Page<ApplicationDTO> handleSearch(SearchObject<String> criteria)
        throws Exception
    {

        Specification<Application> spec = getSpecification(criteria.getCriteria());
        Sort sort = SortOrderFactory.createSortOrder(criteria.getSortings());
        PageRequest pageRequest = 
                sort == null ?
                PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort) :
                PageRequest.of(criteria.getPageNumber(), criteria.getPageSize());

        Page<Application> page = applicationRepository.findAll(spec, pageRequest);

        Page<ApplicationDTO> dtos = page.map(applicationDao::toApplicationDTO);

        return dtos;
    }

}