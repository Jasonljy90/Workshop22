package Jason.Day22_Workshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import Jason.Day22_Workshop.model.RSVP;
import Jason.Day22_Workshop.repository.RsvpRepoImpl;

@RestController
@RequestMapping("/api/rsvp")
public class RSVPController {

    @Autowired
    RsvpRepoImpl rsvpRepoImpl;

    @GetMapping(path = { "/", "/home" }, produces = "application/json")
    public ResponseEntity<List<RSVP>> getAll() {

        List<RSVP> resultList = new ArrayList<RSVP>();
        resultList = rsvpRepoImpl.findAll();

        if (resultList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<RSVP> getByName(@RequestParam String name) { // http://localhost:8080/api/rsvp?name=jason ->
                                                                       // Name cannot have space
        RSVP rsvp = rsvpRepoImpl.findByName(name);

        if (rsvp.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rsvp, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> saveRSVP(@RequestBody RSVP rsvp) {
        Boolean saved = rsvpRepoImpl.save(rsvp);

        if (saved) {
            return new ResponseEntity<>("RSVP record created successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("RSVP record failed to create.", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    // PUT /api/rsvp/fred@gmail.com
    // Content-Type: application/x-www-form-urlencoded
    // Accept: application/json
    @PutMapping("/{email}")
    public ResponseEntity<String> updateRSVP(@PathVariable("email") String email, @RequestBody RSVP rsvp) {

        // Get the data in database by email
        RSVP r = rsvpRepoImpl.findByEmail(email);
        Boolean result = false;

        // if r is not null means got a record in db then we can update the data
        if (r != null) {
            Integer id = r.getId();
            result = rsvpRepoImpl.update(rsvp, id);
        }

        // if result returns true means update successful
        if (result) {
            return new ResponseEntity<>("RSVP record updated successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("RSVP record failed to update.", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getRSVPCount() {
        Integer rsvpCount = rsvpRepoImpl.countAll();
        return new ResponseEntity<>(rsvpCount, HttpStatus.OK);
    }

}
