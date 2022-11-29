package com.virtualmarathon.core.service;

import com.virtualmarathon.core.dao.EventDao;
import com.virtualmarathon.core.dao.TrackingDetailsDao;
import com.virtualmarathon.core.dao.UserDao;
import com.virtualmarathon.core.entity.Event;
import com.virtualmarathon.core.entity.TrackingDetails;
import com.virtualmarathon.core.entity.User;
import com.virtualmarathon.core.exception.trackingDetails.TrackingDetailsNotFoundException;
import com.virtualmarathon.core.exception.trackingDetails.WrongEventException;
import com.virtualmarathon.core.exception.trackingDetails.WrongParticipantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrackingDetailsService {

    @Autowired
    private TrackingDetailsDao trackingDetailsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private EmailSenderServices senderServices;

    public void sendMailAfterRegistration(String userName, Integer eventId){
        Event event = eventDao.findById(eventId).get();
        String title = event.getTitle();
        String gmail = userDao.findById(userName).get().getGmail();
        String organizerName = event.getOrganizer().getUserFullName();
        String line1 = userDao.findById(userName).get().getUserFullName() + ", You have successfully registered in " + title;
        String line2 =  "Here are the Event Details: ";
        String line3 = "Event name: " + event.getTitle();
        String line4 = "Event start date: " + event.getStartDate();
        String line5 = "Event end date: " +  event.getEndDate();
        String typeSmall = event.getType();
        String type = typeSmall.substring(0, 1).toUpperCase() + typeSmall.substring(1);
        String line6 = "Activity: " + type;
        String line7 = "Distance to be covered: " + event.getDistance() + " km";
        String line8 = "This event is being conducted by: " + organizerName;
        String line9 = "We will be waiting for you on the event day!!!";
        String body = line1 + "\n\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6 + "\n" + line7 + "\n" + line8 + "\n\n" + line9;
        String subject = "Hello from Team Virtual Marathon!";
        senderServices.sendEmail(gmail, body, subject);
    }

    public void sendMailAfterEventCompletionToParticipant(String userName, Integer eventId, TrackingDetails existingTrackingDetails) {
        Event event = eventDao.findById(eventId).get();
        String title = event.getTitle();
        String gmail = userDao.findById(userName).get().getGmail();

        String line1 = "Hi " + userDao.findById(userName).get().getUserFullName() + ",";
        String line2 = "Thanks for attending " + title  + " event.";
        String line3 = "We hope you enjoyed our event.";
        String line4 = "Here are your performance statistics: ";
        String typeSmall = event.getType();
        String type = typeSmall.substring(0, 1).toUpperCase() + typeSmall.substring(1);
        String line5 = "Activity: " + type;
        String line6 = "Distance covered: " + existingTrackingDetails.getDistance() + " m";
        String line7 = "Time taken: " + existingTrackingDetails.getTime()  + " sec";
        String line8 = "Speed: " + existingTrackingDetails.getSpeed() + " m/s";
        String line9 = "We look forward to see you again";
        String line10 = "For upcoming events, registration information will be available on our website.";
        String line11 = "https://vertualmarathon-frontend-urtjok3rza-wl.a.run.app";
        String body = line1 + "\n" + line2 + "\n" + line3 + "\n\n" + line4 + "\n" + line5 + "\n" + line6 + "\n" + line7 + "\n" + line8 + "\n\n" + line9 + "\n" + line10 + "\n" +line11;
        String subject = "Thank you for making our event successfull!!";
        senderServices.sendEmail(gmail, body, subject);
    }

    public void sendMailAfterEventCompletionToOrganizer(String userName, Integer eventId, TrackingDetails existingTrackingDetails) {
        Event event = eventDao.findById(eventId).get();
        String title = event.getTitle();
        String gmail = eventDao.findById(eventId).get().getOrganizer().getGmail();
        String organizer = eventDao.findById(eventId).get().getOrganizer().getUserFullName();
        String userFullName = userDao.findById(userName).get().getUserFullName();
        String userFirstName = userFullName.split("\\s")[0];
        String line1 = "Hi " + organizer + ",\n" + "Feedback given by " + userFullName + " after completing " + title + " event.";
        String line3 = "Difficulty: " + existingTrackingDetails.getDifficulty();
        String line4 = "Satisfaction: " + existingTrackingDetails.getSatisfaction();
        String line5 = "Ease of access: " + existingTrackingDetails.getEase();
        String line6 = "Team Virtual Marathon is looking forward to host many more events conducted by you.";
        String body = line1  + "\n\n" + line3 + "\n" + line4 + "\n" + line5 +  "\n\n" + line6;
        String subject = userFirstName + "'s Feedback ";
        senderServices.sendEmail(gmail, body, subject);
    }

    public String getAlphaNumericString(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public void sendMailToTopPerformers(Integer eventId) throws MessagingException {
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        Date currentDate = new Date();
        if(optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            Date endDate = event.getEndDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DATE, 1);
            Date rewardDate = cal.getTime();
            LocalDate currentDateOnly = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate rewardDateOnly = rewardDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            rewardDateOnly = rewardDateOnly.minusYears(1900);
            if(rewardDateOnly.compareTo(currentDateOnly) == 0) {
                List<TrackingDetails> trackingDetails=trackingDetailsDao.findByEventOrderByTimeAsc(event);
                String voucherCompanyName[] = {"Paytm", "Big Bazaar", "Amazon"};
                ClassPathResource voucherPath[] = {
                        new ClassPathResource("/Images/PaytmVoucher.jpg") ,
                        new ClassPathResource("/Images/BigBazzarVoucher.jpg"),
                        new ClassPathResource("/Images/AmazonVoucher.png")
                };
                for(int i=0 ; i< trackingDetails.size() && i<=2 ; i++){
                    String voucher = getAlphaNumericString(20);
                    Optional<User> optionalUser = userDao.findById(trackingDetails.get(i).getParticipant().getUserName());
                    int c = i+1;
                    String line1 = "<p>Congratulations, " + optionalUser.get().getUserFullName() +"</p>";
                    String line2 = "<p>You came to position " + c + " and you got an " + voucherCompanyName[i] + " pay voucher." +"</p>";
                    String line3 = "<p>Redeem Code : " + voucher + "</p>";
                    String line4 = "<html><body><img src='cid:identifier1'></body></html>";
                    String body = line1 + line2 + line3 + line4;

                    String subject = "Rewards, you are in our top performers list";

                    if(optionalUser.isPresent()){
                        String gmail = optionalUser.get().getGmail();
                        senderServices.sendEmailWithInlineImage(gmail,body,subject,voucherPath[i]);
                    }
                }
            }
        }
        else throw new WrongEventException("Wrong Event with id: " + eventId);

    }


    public TrackingDetails addNewTrackingDetails(String userName, Integer eventId, TrackingDetails trackingDetails) {

        User participant;
        Optional<User> optionalUser = userDao.findById(userName);
        if(optionalUser.isPresent()) participant = optionalUser.get();
        else throw new WrongParticipantException("Wrong participant with userName:" + userName);

        Event event;
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        if(optionalEvent.isPresent()) event = optionalEvent.get();
        else throw new WrongEventException("Wrong Event with id: " + eventId);

        trackingDetails.setParticipant(participant);
        trackingDetails.setEvent(event);

        sendMailAfterRegistration(userName, eventId);

        return trackingDetailsDao.save(trackingDetails);

    }

    public List<TrackingDetails> showAllTrackingDetails() {
        return trackingDetailsDao.findAll();
    }

    public TrackingDetails showTrackingDetailsById(Integer trackingDetailsId) {
        Optional<TrackingDetails> optionalTrackingDetails = trackingDetailsDao.findById(trackingDetailsId);
        if(optionalTrackingDetails.isPresent()) return optionalTrackingDetails.get();
        else throw new TrackingDetailsNotFoundException("Tracking Details not found for id: " + trackingDetailsId);
    }

    public TrackingDetails showTrackingDetailsByParticipantNameAndEventId(String userName, Integer eventId) {

        User participant;
        Optional<User> optionalUser = userDao.findById(userName);
        if(optionalUser.isPresent()) participant = optionalUser.get();
        else throw new WrongParticipantException("Wrong participant with userName:" + userName);

        Event event;
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        if(optionalEvent.isPresent()) event = optionalEvent.get();
        else throw new WrongEventException("Wrong Event with id: " + eventId);

        return trackingDetailsDao.findByParticipantAndEvent(participant, event);

    }

    public List<TrackingDetails> showTrackingDetailsByEventId(Integer eventId) {
        Event event;
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        if(optionalEvent.isPresent()) event = optionalEvent.get();
        else throw new WrongEventException("Wrong Event with id: " + eventId);
        return trackingDetailsDao.findByEventOrderByTimeAsc(event);

    }

    public List<TrackingDetails> showTrackingDetailsByParticipantName(String userName) {

        User participant;
        Optional<User> optionalUser = userDao.findById(userName);
        if(optionalUser.isPresent()) participant = optionalUser.get();
        else throw new WrongParticipantException("Wrong participant with userName:" + userName);

        return trackingDetailsDao.findByParticipant(participant);

    }

    public TrackingDetails changeTrackingDetails(String userName, Integer eventId, TrackingDetails updatedTrackingDetails) {

        User participant;
        Optional<User> optionalUser = userDao.findById(userName);
        if(optionalUser.isPresent()) participant = optionalUser.get();
        else throw new WrongParticipantException("Wrong participant with userName:" + userName);

        Event event;
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        if(optionalEvent.isPresent()) event = optionalEvent.get();
        else throw new WrongEventException("Wrong Event with id: " + eventId);

        TrackingDetails existingTrackingDetails = trackingDetailsDao.findByParticipantAndEvent(participant, event);
        existingTrackingDetails.setDistance(updatedTrackingDetails.getDistance());
        existingTrackingDetails.setTime(updatedTrackingDetails.getTime());
        existingTrackingDetails.setSpeed(updatedTrackingDetails.getSpeed());
        existingTrackingDetails.setHasCompletedEvent(updatedTrackingDetails.getHasCompletedEvent());
        existingTrackingDetails.setDifficulty(updatedTrackingDetails.getDifficulty());
        existingTrackingDetails.setSatisfaction(updatedTrackingDetails.getSatisfaction());
        existingTrackingDetails.setEase(updatedTrackingDetails.getEase());

        if(existingTrackingDetails.getDifficulty() == 0 && existingTrackingDetails.getSatisfaction() == 0 && existingTrackingDetails.getEase() == 0) {
            sendMailAfterEventCompletionToParticipant(userName, eventId, existingTrackingDetails);
        } else {
            sendMailAfterEventCompletionToOrganizer(userName, eventId, existingTrackingDetails);
        }


        return trackingDetailsDao.save(existingTrackingDetails);

    }

    @Transactional
    public void removeTrackingDetails(String userName, Integer eventId) {

        User participant;
        Optional<User> optionalUser = userDao.findById(userName);
        if(optionalUser.isPresent()) participant = optionalUser.get();
        else throw new WrongParticipantException("Wrong participant with userName:" + userName);

        Event event;
        Optional<Event> optionalEvent = eventDao.findById(eventId);
        if(optionalEvent.isPresent()) event = optionalEvent.get();
        else throw new WrongEventException("Wrong Event with id: " + eventId);

        trackingDetailsDao.deleteByParticipantAndEvent(participant, event);

    }

    public void removeAllTrackingDetails() {
        trackingDetailsDao.deleteAll();
    }

}
