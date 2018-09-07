package start;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AlienRepo extends CrudRepository<Alien,Integer>
{
  

  Boolean existsByAnameAndPassword(String aname,String password);
  Boolean existsByAname(String aname);	
  Boolean existsByAidAndAname(int aid,String aname);
  
  Alien findByAnameAndPassword(String aname,String password);
  Alien findByAid(int id);
  Alien findByAname(String aname);
  Alien findByAnameAndAid(String aname,int aid);
  
 }

