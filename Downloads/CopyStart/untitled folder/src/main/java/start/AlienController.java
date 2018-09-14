package start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.attoparser.config.ParseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;





@Controller
public class AlienController {

	public int userLogedInID;

	@Autowired
	FileStorageImpl fileStorage;
	List<FileInfo> list = new ArrayList<>();
	List<FileInfo> savedFiles;
	List<FileInfo> fileInfos;
	List<FileInfo> listfiles;
	List<FileInfo> userFiles;
	@Autowired
	AlienRepo repo;

	@Autowired
	FileRepo filerepo;

	public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

	@RequestMapping("/home")
	public String home() {
		return "home";
	}

	@RequestMapping("/addAlien")
	public String addAlien(Alien alien,Model model)

	{
		if(alien.getAname()!=null)
		{
			if(alien.getPassword()!=null)
		{
		repo.save(alien);

		Alien obj = repo.findByAnameAndPassword(alien.getAname(), alien.getPassword());
		userLogedInID = obj.getAid();
		  model.addAttribute("file",listfiles);
		
		return "loginpage";
        }
			else 
				return "message";
			
			}
		else return "message";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String login(@RequestParam String aname, @RequestParam String password,Model model) {

		if (repo.existsByAnameAndPassword(aname, password)) {
			Alien obj = repo.findByAnameAndPassword(aname, password);
			userLogedInID = obj.getAid();
		            listfiles =obj.getFiles();
		    //  for (FileInfo fle:listfiles)
		     // {
		    //	  System.out.println(fle.getFilename());
		    	 
		    //  }
		       
		         savedFiles=filerepo.findFilesByAlien(obj);
		     
		      model.addAttribute("file",savedFiles);
		      
			
			return "userhome";
		} else {
			return "message";
		}

	}


	@RequestMapping("/addfilestouser")
	public String filesToOtherUser(@RequestParam String aname ,@RequestParam int aid,@RequestParam String filename,Model model)
	{
		
		FileInfo fi=filerepo.findByFilename(filename);
		
		
		if(repo.existsByAidAndAname(aid, aname))
		{
		Alien a= repo.findByAid(aid);
		fi.setAlien(a);
		filerepo.save(fi);
		
		model.addAttribute("msg", "Successfully Shared With Desired User");	
		
		}
		
         return "message1"; 
		
		}



	@RequestMapping("/showAlien")
	public String userHome() {
		return "showAlien";
	}

	@RequestMapping("/userfiles")
	public String userFiles() {
		return "userfiles";
	}

	// Upload controller Starts

	/*
	 * @RequestMapping("/") public String upload(Model model) { return "uploadpage";
	 * }
	 */
	@RequestMapping(path="/upload",method=RequestMethod.POST)
	public String upload(Model model, @RequestParam("files") MultipartFile[] files) {
		StringBuilder filenames = new StringBuilder();

		Alien myobj = repo.findByAid(userLogedInID);
		
		

		for (MultipartFile file : files) {
			Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
			filenames.append(file.getOriginalFilename());

			FileInfo saveObj = new FileInfo();
			int randomNum = ThreadLocalRandom.current().nextInt(0, 5555 + 1);

			saveObj.setId(randomNum);
			saveObj.setFilename(file.getOriginalFilename());
			saveObj.setUrl(uploadDirectory);
			saveObj.setAlien(myobj);
			filerepo.save(saveObj);

			
			list.add(saveObj);
			myobj.setFiles(list);

			repo.save(myobj);

			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		

		model.addAttribute("msg", "SuccessFully uploaded Files" + filenames.toString());
   
		
		  userFiles = filerepo.findFilesByAlien(myobj); 
		

		 model.addAttribute("file",userFiles);

		return "userhome";

	}

	// Download Controller Starts

	/*
	 * @GetMapping("/files") public String getListFiles(Model model) {
	 * List<FileInfo> fileInfos = fileStorage.loadFiles().map( path -> { String
	 * filename = path.getFileName().toString(); String url =
	 * MvcUriComponentsBuilder.fromMethodName(AlienController.class, "downloadFile",
	 * path.getFileName().toString()).build().toString(); return new
	 * FileInfo(filename, url); } ) .collect(Collectors.toList());
	 * 
	 * model.addAttribute("files", fileInfos); return "listfiles"; }
	 * 
	 */

	/*
	 * Download Files
	 */

	/*
	 * @RequestMapping("/share") public String share(@RequestParam String
	 * aname,@RequestParam String filename,@RequestParam("files") MultipartFile[]
	 * files) { if(repo.existsByAname(aname)) {
	 * 
	 * 
	 * } StringBuilder filenames=new StringBuilder(); for(MultipartFile file:files)
	 * { Path fileNameAndPath=Paths.get(user1Directory,file.getOriginalFilename());
	 * filenames.append(file.getOriginalFilename()); try {
	 * Files.write(fileNameAndPath,file.getBytes()); } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 */
	
	@GetMapping("/files")
     public String DownloadFile(Model model)
     {
		
	List<FileInfo> fileInfos = userFiles.stream().map(
				path ->	{
					String filename="b ";
					String url="b ";
					for(FileInfo f:userFiles)
					{
						if(f.getFilename()==path.getFilename().toString())
						{
					     filename = path.getFilename().toString();
					         url = MvcUriComponentsBuilder.fromMethodName(AlienController.class,
	                        "downloadFile", path.getFilename().toString()).build().toString();
					}
						else
						{
							filename="a ";
							 url=" a";
						}}
					return new FileInfo(filename, url); 
				}
					
			)
			.collect(Collectors.toList());
		
	
	model.addAttribute("file", fileInfos);
		/*for(FileInfo f:userFiles) {
		String url = MvcUriComponentsBuilder.fromMethodName(AlienController.class,
                "downloadFile", f.getFilename().toString()).build().toString();
	model.addAttribute("file",userFiles);*/
		
	return "downloadfile";

	
     }
	@GetMapping("/files/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		Resource file = fileStorage.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	
	
	
	
	
@RequestMapping("/backtohome")
public String back()
{
	return "userhome"; 
}


}
