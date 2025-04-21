// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: TauSecure::backend::bw.co.roguesystems.tau::authorisation::AuthorisationService
 * STEREOTYPE:  Service
 */
package bw.co.roguesystems.tau.authorisation;

import bw.co.roguesystems.tau.PropertySearchOrder;
import bw.co.roguesystems.tau.SearchObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
 * @see bw.co.roguesystems.tau.authorisation.AuthorisationService
 */
@Service("authorisationService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class AuthorisationServiceImpl
    extends AuthorisationServiceBase
{
    public AuthorisationServiceImpl(
        AuthorisationDao authorisationDao,
        AuthorisationRepository authorisationRepository,
        MessageSource messageSource
    ) {
        
        super(
            authorisationDao,
            authorisationRepository,
            messageSource
        );
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#findById(Long)
     */
    @Override
    protected AuthorisationDTO handleFindById(String id)
            throws Exception {

        if (id == null) {
            return null;
        }

        Authorisation authorisation = this.authorisationRepository.getReferenceById(id);
        return authorisationDao.toAuthorisationDTO(authorisation);
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#save(AuthorisationDTO)
     */
    @Override
    protected AuthorisationDTO handleSave(AuthorisationDTO authorisation)
            throws Exception {

        Authorisation entity = authorisationDao.authorisationDTOToEntity(authorisation);
        entity = authorisationRepository.save(entity);

        AuthorisationDTO dto = authorisationDao.toAuthorisationDTO(entity);

        Map<String, Object> evictions = new HashMap<>();
        evictions.put("authorisation", null);
        // cacheManagement.evict(evictions);
        // cacheManagement.put("authorisation", dto.getId(), dto);

        return dto;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#remove(Long)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if (id == null) {
            return false;
        }

        this.authorisationRepository.deleteById(id);

        return true;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#getAll()
     */
    @Override
    protected Collection<AuthorisationListDTO> handleGetAll()
            throws Exception {

        Collection<Authorisation> authorisations = authorisationRepository.findAll();
        return authorisationDao.toAuthorisationListDTOCollection(authorisations);
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#search(AuthorisationCriteria)
     */
    @Override
    protected Collection<AuthorisationListDTO> handleSearch(AuthorisationCriteria criteria,
            final Set<PropertySearchOrder> orderings)
            throws Exception {
        if (criteria == null) {
            return null;
        }

        Set<String> roles = new HashSet<>();
        if (criteria.getRoles() != null) {
            roles.addAll(criteria.getRoles());
        }

        if (criteria.getAccessPointName() == null) {
            criteria.setAccessPointName("");
        }

        if (criteria.getAccessPointUrl() == null) {
            criteria.setAccessPointUrl("");
        }

        Collection<AuthorisationListDTO> authorisations = roles.isEmpty()
                ? authorisationRepository.searchNoRoles(criteria.getAccessPointName(), criteria.getAccessPointUrl())
                : authorisationRepository.search(criteria.getAccessPointName(), criteria.getAccessPointUrl(), roles);

        return authorisations;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<AuthorisationListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {
        Page<Authorisation> authorisations = null;

        if (pageNumber < 0 || pageSize < 1) {

        } else {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
            authorisations = authorisationRepository.findAll(pageable);
        }

        return authorisations == null ? null
                : authorisations.map(auth -> authorisationDao.toAuthorisationListDTO(auth));
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#getAccessTypeCodeAuthorisations(Set<String>,
     *      Set<String>)
     */
    @Override
    protected Collection<AuthorisationListDTO> handleGetAccessTypeCodeAuthorisations(Set<String> roles,
            Set<String> accessPointTypeCodes)
            throws Exception {

        Collection<AuthorisationListDTO> auths = this.authorisationDao
                .toAuthorisationListDTOCollection(this.authorisationRepository.findAccessTypeCodeAuthorisations(roles,
                        accessPointTypeCodes));

        String hash = roles.hashCode() + "" + accessPointTypeCodes.hashCode();
        // cacheManagement.put("authorisation", hash, auths);

        return auths;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#findByRolesAndUrl(String,
     *      Set<String>)
     */
    @Override
    protected Collection<AuthorisationListDTO> handleFindByRolesAndUrl(String url, Set<String> roles)
            throws Exception {
        Collection<AuthorisationListDTO> authsdtos = this.authorisationRepository.findByRolesAndUrl(url, roles);

        return authsdtos;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#findByUrlPrefix(String)
     */
    @Override
    protected Collection<AuthorisationListDTO> handleFindByUrlPrefix(String prefix)
            throws Exception {

        Collection<AuthorisationListDTO> auths = this.authorisationRepository.findByUrlPrefix(prefix);

        return auths;
    }

    @Override
    protected Collection<AuthorisationListDTO> handleFindByUrlPrefixAndRoles(String prefix, Set<String> roles)
            throws Exception {

        Collection<AuthorisationListDTO> auths = this.authorisationRepository.findByUrlPrefixAndRoles(prefix, roles);

        return auths;
    }

    @Override
    protected Collection<AuthorisationListDTO> handleFindByAccessUrlAndTypeCode(String url, String code)
            throws Exception {

        return this.authorisationRepository.findByAccessUrlAndTypeCode(url, code);
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#search(SearchObject<AuthorisationCriteria>)
     */
    @Override
    protected Page<AuthorisationListDTO> handleSearch(SearchObject<AuthorisationCriteria> criteria)
            throws Exception {

        if (criteria == null) {
            return null;
        }

        Set<String> roles = new HashSet<>();

        if (criteria.getCriteria().getRoles() != null) {
            roles.addAll(criteria.getCriteria().getRoles());

        }

        String accessPointName = criteria.getCriteria().getAccessPointName();

        if (accessPointName == null) {
            accessPointName = "";
        }

        String accessPointUrl = criteria.getCriteria().getAccessPointUrl();

        if (accessPointUrl == null) {
            accessPointUrl = "";
        }

        String accessPointType = criteria.getCriteria().getAccessPointType();

        if (accessPointType == null) {
            accessPointType = "";
        }

        Page<AuthorisationListDTO> authorisations = roles.isEmpty()
                ? this.authorisationRepository.searchNoRoles(accessPointName, accessPointUrl,
                        PageRequest.of(criteria.getPageNumber(), criteria.getPageSize()))
                : this.authorisationRepository.search(
                        criteria.getCriteria().getAccessPointName(), criteria.getCriteria().getAccessPointUrl(), roles,
                        PageRequest.of(criteria.getPageNumber(), criteria.getPageSize()));


        return authorisations;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#getAccessTypeCodeAuthorisations(Set<String>,
     *      Set<String>, Integer, Integer)
     */
    @Override
    protected Page<AuthorisationListDTO> handleGetAccessTypeCodeAuthorisations(Set<String> roles,
            Set<String> accessPointTypeCodes, Integer pageNumber, Integer pageSize)
            throws Exception {

        Page<Authorisation> authorisations = this.authorisationRepository.findAccessTypeCodeAuthorisations(roles,
                accessPointTypeCodes, PageRequest.of(pageNumber, pageSize));

        Page<AuthorisationListDTO> p = authorisations == null ? null
                : authorisations.map(auth -> authorisationDao.toAuthorisationListDTO(auth));

        return p;
    }

    /**
     * @see bw.co.bitri.cfpso.authorisation.AuthorisationService#findByRolesAndUrl(String,
     *      Set<String>, Integer, Integer)
     */
    @Override
    protected Page<AuthorisationListDTO> handleFindByRolesAndUrl(String url, Set<String> roles, Integer pageNumber,
            Integer pageSize)
            throws Exception {

        Page<AuthorisationListDTO> p = this.authorisationRepository.findByRolesAndUrl(url, roles,
                PageRequest.of(pageNumber, pageSize));
        
        return p;
    }

    @Override
    protected Collection<Page<AuthorisationListDTO>> handleFindApplicationAuthorisations(String applicationId,
            Integer pageNumber, Integer pageSize) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindApplicationAuthorisations'");
    }

    @Override
    protected Collection<AuthorisationListDTO> handleFindApplicationAuthorisations(String applicationId)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindApplicationAuthorisations'");
    }

}