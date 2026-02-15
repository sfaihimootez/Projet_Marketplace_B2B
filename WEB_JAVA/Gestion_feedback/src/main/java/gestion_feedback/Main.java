package gestion_feedback;

import gestion_feedback.model.Feedback;
import gestion_feedback.model.Reponse;
import gestion_feedback.service.FeedbackService;
import gestion_feedback.service.ReponseService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FeedbackService feedbackService = new FeedbackService();
    private static final ReponseService reponseService = new ReponseService();

    public static void main(String[] args) {
        System.out.println("=== GESTION FEEDBACK - CONSOLE ===");

        while (true) {
            afficherMenu();
            String choix = scanner.nextLine().trim();

            try {
                switch (choix) {
                    case "1" -> ajouterFeedback();
                    case "2" -> afficherFeedbacks();
                    case "3" -> ajouterReponse();
                    case "4" -> afficherReponses();
                    case "5" -> supprimerFeedback();
                    case "6" -> modifierFeedback();
                    case "0" -> {
                        System.out.println("Au revoir !");
                        return;
                    }
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void afficherMenu() {
        System.out.println("\nMenu :");
        System.out.println("1. Ajouter feedback");
        System.out.println("2. Lister feedbacks");
        System.out.println("3. Ajouter réponse");
        System.out.println("4. Lister réponses d'un feedback");
        System.out.println("5. Supprimer feedback");
        System.out.println("6. Modifier feedback");
        System.out.println("0. Quitter");
        System.out.print("Choix : ");
    }

    private static void ajouterFeedback() {
        System.out.print("Commentaire : ");
        String commentaire = scanner.nextLine().trim();
        System.out.print("Note : ");
        String note = scanner.nextLine().trim();
        System.out.print("Date (YYYY-MM-DD) : ");
        String dateStr = scanner.nextLine().trim();

        try {
            Date date = Date.valueOf(LocalDate.parse(dateStr));
            Feedback f = new Feedback(commentaire, note, date);
            if (feedbackService.ajouter(f)) {
                System.out.println("Ajout OK ! ID = " + f.getId());
            } else {
                System.out.println("Échec ajout.");
            }
        } catch (Exception e) {
            System.out.println("Erreur saisie ou format date.");
        }
    }

    private static void afficherFeedbacks() {
        List<Feedback> list = feedbackService.getAll();
        if (list.isEmpty()) {
            System.out.println("Aucun feedback.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void ajouterReponse() {
        int id = readInt("ID feedback : ");
        String contenu = readString("Contenu : ");
        String dateStr = readString("Date (YYYY-MM-DD) : ");

        try {
            Date date = Date.valueOf(LocalDate.parse(dateStr));
            Reponse r = new Reponse(contenu, date, id);
            if (reponseService.ajouter(r)) {
                System.out.println("Réponse ajoutée !");
            } else {
                System.out.println("Échec ajout.");
            }
        } catch (Exception e) {
            System.out.println("Erreur saisie.");
        }
    }

    private static void afficherReponses() {
        int id = readInt("ID feedback : ");
        List<Reponse> list = reponseService.getByFeedbackId(id);
        if (list.isEmpty()) {
            System.out.println("Aucune réponse.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void supprimerFeedback() {
        int id = readInt("ID à supprimer : ");
        if (feedbackService.supprimer(id)) {
            System.out.println("Supprimé !");
        } else {
            System.out.println("ID non trouvé ou erreur.");
        }
    }

    private static void modifierFeedback() {
        int id = readInt("ID à modifier : ");
        Feedback f = feedbackService.getById(id);
        if (f == null) {
            System.out.println("Feedback non trouvé.");
            return;
        }

        System.out.println("Actuel : " + f);
        System.out.println("Laissez vide pour garder");

        String commentaire = readString("Nouveau commentaire : ");
        String note = readString("Nouvelle note : ");
        String dateStr = readString("Nouvelle date (YYYY-MM-DD) : ");

        if (!commentaire.isEmpty()) f.setCommentaire(commentaire);
        if (!note.isEmpty()) f.setNote(note);
        if (!dateStr.isEmpty()) {
            try {
                f.setDateStatut(Date.valueOf(LocalDate.parse(dateStr)));
            } catch (Exception e) {
                System.out.println("Date ignorée (format invalide).");
            }
        }

        if (feedbackService.update(f)) {
            System.out.println("Modifié !");
        } else {
            System.out.println("Échec modification.");
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Nombre invalide, réessayez : ");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}