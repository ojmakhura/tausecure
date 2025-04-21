// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: TauSecure::backend::bw.co.roguesystems.tau::access::AccessPointService
 * STEREOTYPE:  Service
 */
package bw.co.roguesystems.tau.access;

import bw.co.roguesystems.tau.PropertySearchOrder;
import bw.co.roguesystems.tau.SearchObject;
import bw.co.roguesystems.tau.SortOrderFactory;

import java.util.Collection;
import java.util.Set;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.roguesystems.tau.access.AccessPointService
 */
@Service("accessPointService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class AccessPointServiceImpl
    extends AccessPointServiceBase
{
    public AccessPointServiceImpl(
        AccessPointDao accessPointDao,
        AccessPointRepository accessPointRepository,
        MessageSource messageSource
    ) {
        
        super(
            accessPointDao,
            accessPointRepository,
            messageSource
        );
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#findById(Long)
     */
    @Override
    protected AccessPointDTO handleFindById(String id)
            throws Exception {

        AccessPoint accessPoint = accessPointRepository.findById(id).orElse(null);
        return accessPoint != null ? accessPointDao.toAccessPointDTO(accessPoint) : null;
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#getAll()
     */
    @Override
    protected Collection<AccessPointListDTO> handleGetAll()
            throws Exception {

        return accessPointDao.toAccessPointListDTOCollection(accessPointRepository.findAll());
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#getAll(Integer, Integer)
     */
    @Override
    protected Page<AccessPointListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        Page<AccessPoint> points = accessPointRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return points.map(accessPoint -> accessPointDao.toAccessPointListDTO(accessPoint));
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#remove(Long)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        accessPointRepository.deleteById(id);
        return true;
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#save(AccessPointDTO)
     */
    @Override
    protected AccessPointDTO handleSave(AccessPointDTO accessPoint)
            throws Exception {

        AccessPoint point = accessPointDao.accessPointDTOToEntity(accessPoint);
        point = accessPointRepository.save(point);
        return accessPointDao.toAccessPointDTO(point);
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#search(AccessPointCriteria)
     */
    @Override
    protected Collection<AccessPointListDTO> handleSearch(AccessPointCriteria criteria,
            final Set<PropertySearchOrder> orderings)
            throws Exception {

        return accessPointRepository.search(criteria.getName(), criteria.getUrl(), criteria.getTypeId(),
                criteria.getType());
    }

    /**
     * @see bw.co.bitri.cfpso.access.AccessPointService#search(SearchObject<AccessPointCriteria>)
     */
    @Override
    protected Page<AccessPointListDTO> handleSearch(SearchObject<AccessPointCriteria> criteria)
            throws Exception {

        AccessPointCriteria accessPointCriteria = criteria.getCriteria();
        Sort sort = SortOrderFactory.createSortOrder(criteria.getSortings());
        Pageable pageable = sort == null ? PageRequest.of(criteria.getPageNumber(), criteria.getPageSize())
                : PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);

        return accessPointRepository.search(accessPointCriteria.getName(), accessPointCriteria.getUrl(),
                accessPointCriteria.getTypeId(), accessPointCriteria.getType(), pageable);
    }

    @Override
    protected Collection<AccessPointListDTO> handleSearchOr(AccessPointCriteria criteria,
            Set<PropertySearchOrder> orderings) throws Exception {
        
        return accessPointRepository.searchOr(criteria.getName(), criteria.getUrl(), criteria.getTypeId(), criteria.getType());
    }

    @Override
    protected Page<AccessPointListDTO> handleSearchOr(SearchObject<AccessPointCriteria> criteria) throws Exception {
        // TODO Auto-generated method stub
        
        AccessPointCriteria accessPointCriteria = criteria.getCriteria();
        Sort sort = SortOrderFactory.createSortOrder(criteria.getSortings());
        Pageable pageable = sort == null ? PageRequest.of(criteria.getPageNumber(), criteria.getPageSize())
                : PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);

        return accessPointRepository.searchOr(accessPointCriteria.getName(), accessPointCriteria.getUrl(), accessPointCriteria.getTypeId(), accessPointCriteria.getType(), pageable);
    }

    @Override
    protected Collection<AccessPointListDTO> handleFindApplicationAccessPoints(String applicationId) throws Exception {
        
        return accessPointRepository.findApplicationAccessPoints(applicationId);
    }

    @Override
    protected Page<AccessPointListDTO> handleFindApplicationAccessPoints(String applicationId,
            Integer pageNumber, Integer pageSize) throws Exception {
        
        return accessPointRepository.findApplicationAccessPoints(applicationId, PageRequest.of(pageNumber, pageSize));
        
    }
}