package start;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepo extends CrudRepository<ShareFiles,Integer> {

	
	
}
