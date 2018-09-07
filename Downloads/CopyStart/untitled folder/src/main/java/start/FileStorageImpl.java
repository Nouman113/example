package start;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageImpl {
	@Autowired
	AlienController cont;
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	 Path rootLocation=Paths.get("uploads");

		
		public  void location()
		{
			for(int i=1;i<=10;i++)
			{
			if (cont.userLogedInID==i)
			{
				byte[] array = new byte[7]; // length is bounded by 7
			    new Random().nextBytes(array);
			    String generatedString = new String(array, Charset.forName("UTF-8"));
			    
			    System.out.println(generatedString);
			   rootLocation=Paths.get(generatedString);
			    
			}}
		}

	public void store(MultipartFile file){
		try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
        	throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
	}
	
	
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
            	throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
        	throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
    }
    
	
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
   
    }

	
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

	
	public Stream<Path> loadFiles() {
        try {
                 return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
        	throw new RuntimeException("\"Failed to read stored file");
        }
	}
	
	
}