package ma.emsi.displayservice.controller;

import ma.emsi.displayservice.dto.ApplicationDTO;
import ma.emsi.displayservice.model.ApplicationE;
import ma.emsi.displayservice.model.Interview;
import ma.emsi.displayservice.model.Interviewer;
import ma.emsi.displayservice.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;


@Controller
public class DisplayController {

    private static final String UPLOAD_DIR = "uploads/";

    private static final Logger logger = LoggerFactory.getLogger(DisplayController.class);


    @Autowired
    private RestTemplate restTemplate;

    // URLs des microservices
    private static final String AUTH_URL = "http://localhost:8081/api/auth";
    private static final String APP_URL  = "http://localhost:8082/api/applications";
    private static final String RH_URL   = "http://localhost:8083/api/rh";
    private static final String INT_URL  = "http://localhost:8086/api/interviewers";

    // Page d'accueil
    @GetMapping("/")
    public String home() {
        return "index"; // templates/index.html
    }

    // ----------------------------
    // Afficher les Candidatures
    // ----------------------------

    @GetMapping("/applications")
    public String showApplications(Model model) {
        try {
            ApplicationE[] appsArray = restTemplate.getForObject(APP_URL, ApplicationE[].class);
            List<ApplicationE> apps = appsArray != null ? Arrays.asList(appsArray) : List.of();
            model.addAttribute("apps", apps);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer les applications.");
            e.printStackTrace(); // Pour le débogage
        }
        return "applications"; // templates/applications.html
    }

    // ----------------------------
    // Afficher les Interviewers
    // ----------------------------

    @GetMapping("/interviewers")
    public String showInterviewers(Model model) {
        try {
            Interviewer[] interviewersArray = restTemplate.getForObject(INT_URL, Interviewer[].class);
            List<Interviewer> interviewers = interviewersArray != null ? Arrays.asList(interviewersArray) : List.of();
            model.addAttribute("interviewers", interviewers);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer les interviewers.");
            e.printStackTrace();
        }
        return "interviewers"; // templates/interviewers.html
    }

    // ----------------------------
    // Afficher les Interviews (RH)
    // ----------------------------

    @GetMapping("/rh")
    public String showRhInterviews(Model model) {
        try {
            Interview[] interviewsArray = restTemplate.getForObject(RH_URL + "/interviews", Interview[].class);
            List<Interview> interviews = interviewsArray != null ? Arrays.asList(interviewsArray) : List.of();
            model.addAttribute("interviews", interviews);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer les interviews.");
            e.printStackTrace();
        }
        return "rh"; // templates/rh.html
    }

    // ----------------------------
    // Afficher les Utilisateurs (Service Authentification)
    // ----------------------------

    @GetMapping("/auth")
    public String showUsers(Model model) {
        try {
            User[] usersArray = restTemplate.getForObject(AUTH_URL, User[].class);
            List<User> users = usersArray != null ? Arrays.asList(usersArray) : List.of();
            model.addAttribute("users", users);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer les utilisateurs.");
            e.printStackTrace();
        }
        return "auth"; // templates/auth.html
    }

    // ----------------------------
    // Afficher les Candidatures Admis
    // ----------------------------

    @GetMapping("/applications/admitted")
    public String showAdmittedApplications(Model model) {
        try {
            // Appeler l'endpoint du rh-service pour récupérer les candidatures admises
            ApplicationDTO[] admittedArray = restTemplate.getForObject(RH_URL + "/applications/admitted", ApplicationDTO[].class);
            List<ApplicationDTO> admittedApps = admittedArray != null ? Arrays.asList(admittedArray) : List.of();
            model.addAttribute("admittedApps", admittedApps);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer les candidatures admises.");
            e.printStackTrace();
        }
        return "admitted_applications"; // templates/admitted_applications.html
    }

    // ----------------------------
    // Afficher les Candidatures Non Admis
    // ----------------------------

    @GetMapping("/applications/not-admitted")
    public String showNotAdmittedApplications(Model model) {
        try {
            // Appeler l'endpoint du rh-service pour récupérer les candidatures non admises
            ApplicationDTO[] notAdmittedArray = restTemplate.getForObject(RH_URL + "/applications/not-admitted", ApplicationDTO[].class);
            List<ApplicationDTO> notAdmittedApps = notAdmittedArray != null ? Arrays.asList(notAdmittedArray) : List.of();
            model.addAttribute("notAdmittedApps", notAdmittedApps);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer les candidatures non admises.");
            e.printStackTrace();
        }
        return "not_admitted_applications"; // templates/not_admitted_applications.html
    }

    // ----------------------------
    // Déclencher les Prédictions
    // ----------------------------

    @GetMapping("/applications/predict")
    public String predictApplications(RedirectAttributes redirectAttributes) {
        try {
            // Appeler l'endpoint de prédiction du rh-service
            restTemplate.postForObject(RH_URL + "/predict", null, ApplicationDTO[].class);
            redirectAttributes.addFlashAttribute("success", "Prédictions effectuées avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la prédiction des candidatures.");
            e.printStackTrace();
        }
        return "redirect:/applications"; // Redirige vers la liste des applications
    }
    @GetMapping("/applications/new")
    public String showCreateApplicationForm(Model model) {
        model.addAttribute("application", new ApplicationE());
        return "application_form"; // templates/application_form.html
    }

    @PostMapping("/applications")
    public String createApplication(@ModelAttribute ApplicationE application, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.postForObject(APP_URL, application, ApplicationE.class);
            redirectAttributes.addFlashAttribute("success", "Candidature créée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création de la candidature.");
        }
        return "redirect:/applications";
    }


    @GetMapping("/applications/edit/{id}")
    public String showEditApplicationForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.debug("Fetching application with ID: {}", id);
            ApplicationE application = restTemplate.getForObject(APP_URL + "/" + id, ApplicationE.class);
            if (application != null) {
                model.addAttribute("application", application);
            } else {
                redirectAttributes.addFlashAttribute("error", "Application non trouvée.");
                return "redirect:/applications";
            }
        } catch (Exception e) {
            logger.error("Error fetching application with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la récupération de l'application.");
            return "redirect:/applications";
        }
        return "application_form"; // templates/application_form.html
    }
    @PostMapping("/applications/update/{id}")
    public String updateApplication(@PathVariable Long id, ApplicationE application, RedirectAttributes redirectAttributes) {
        try {
            logger.debug("Updating application with ID: {}", id);
            restTemplate.put(APP_URL + "/" + id, application);
            redirectAttributes.addFlashAttribute("success", "Application mise à jour avec succès.");
        } catch (Exception e) {
            logger.error("Error updating application with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour de l'application.");
            return "redirect:/applications/edit/" + id;
        }
        return "redirect:/applications";
    }
    @GetMapping("/applications/delete/{id}")
    public String deleteApplication(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            logger.debug("Deleting application with ID: {}", id);
            restTemplate.delete(APP_URL + "/" + id);
            redirectAttributes.addFlashAttribute("success", "Application supprimée avec succès.");
        } catch (Exception e) {
            logger.error("Error deleting application with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression de l'application.");
        }
        return "redirect:/applications";
    }






}
