package dmitry.polyakov.services;

import dmitry.polyakov.constants.BotStateEnum;
import dmitry.polyakov.exceptions.UserNotFoundException;
import dmitry.polyakov.models.User;
import dmitry.polyakov.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Long chatId) throws UserNotFoundException {
        return userRepository.findById(chatId).orElseThrow(() -> new UserNotFoundException(chatId));
    }

    public boolean isUserFoundById(Long chatId) {
        return userRepository.findById(chatId).isPresent();
    }

    public boolean isUserMatchedWithBotState(Long chatId, BotStateEnum userBotState) {
        return userRepository.findById(chatId).isPresent() && userRepository.findById(chatId).get().getUserBotState().equals(userBotState);
    }

    public Set<User> getAllUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public int getCurrentPageNumber(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        return userOptional.map(User::getCurrentPageNumber).orElse(0);
    }
    public String getSavedPhrase(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        return userOptional.map(User::getCurrentPhrase).orElse("");
    }

    public void setCurrentPageNumber(Long chatId, int currentPageNumber) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setCurrentPageNumber(currentPageNumber);
            userRepository.save(user);
        }
    }

    @Transactional
    public void setUserCurrentPhrase(Long chatId, String currentPhrase) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setCurrentPhrase(currentPhrase);
            userRepository.save(user);
        }
    }

/*    public String getBotLanguage(Long chatId) {
        Optional<User> userOptional = userRepository.findById(chatId);
        return userOptional.map(User::getBotLanguage).orElse("English");
    }

    @Transactional
    public void setBotLanguage(Long chatId, String botLanguage) {
        Optional<User> userOptional = userRepository.findById(chatId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setCurrentPhrase(botLanguage);
            userRepository.save(user);
        }
    }*/
}
