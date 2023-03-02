package Jason.Day22_Workshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Jason.Day22_Workshop.model.RSVP;

@Repository
public class RsvpRepoImpl {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String findAllSQL = "select * from rsvp";

    private final String findByNameSQL = "select * from rsvp where full_name like ?";

    private final String findByEmailSQL = "select * from rsvp where email = ?";

    private final String insertSQL = "insert into rsvp (full_name, email, phone, confirmation_date, comments) "
            + "values(?, ?, ?, ?, ?)";

    private final String updateSQL = "update rsvp " +
            "set full_name = ?, email = ?, phone = ?, " +
            "confirmation_date = ?, comments = ? " +
            "where id = ?";

    private final String countSQL = "select count(*) from rsvp";

    private final String findByIdSQL = "select * from rsvp where id = ?";

    public RSVP findById(Integer id) {
        return jdbcTemplate.queryForObject(findByIdSQL, BeanPropertyRowMapper.newInstance(RSVP.class), id);
    }

    public RSVP findByEmail(String email) {
        return jdbcTemplate.queryForObject(findByEmailSQL, BeanPropertyRowMapper.newInstance(RSVP.class), email);
    }

    public List<RSVP> findAll() {
        List<RSVP> resultList = new ArrayList<RSVP>();
        resultList = jdbcTemplate.query(findAllSQL, BeanPropertyRowMapper.newInstance(RSVP.class));
        return resultList;
    }

    public RSVP findByName(String name) {
        RSVP rsvp = new RSVP();
        try {
            rsvp = jdbcTemplate.queryForObject(findByNameSQL, BeanPropertyRowMapper.newInstance(RSVP.class), name);
        } catch (Exception e) {

        }
        return rsvp;
    }

    public Boolean save(RSVP rsvp) {
        Integer result = jdbcTemplate.update(insertSQL, rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(),
                rsvp.getConfirmationDate(), rsvp.getComments());

        if (result == 0) {
            return false;
        }
        return true;
    }

    public Boolean update(RSVP rsvp, Integer id) {
        Integer result = jdbcTemplate.update(updateSQL, rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(),
                rsvp.getConfirmationDate(), rsvp.getComments(), id);

        if (result == 0) {
            return false;
        }
        return true;
    }

    public Integer countAll() {
        Integer result = jdbcTemplate.queryForObject(countSQL, Integer.class);
        return result;
    }

    /*
     * @Transactional
     * public int[] batchInsert(List<RSVP> rsvp) {
     * return jdbcTemplate.batchUpdate(insertSQL, (BatchPreparedStatementSetter) new
     * BatchPreparedStatementSetter() {
     * 
     * // rsvp.getFullName(), rsvp.getEmail(), rsvp.getPhone(),
     * // rsvp.getConfirmationDate(), rsvp.getComments()
     * public void setValues(PreparedStatement ps, int i) throws SQLException {
     * ps.setString(1, rsvp.get(i).getFullName());
     * ps.setString(2,rsvp.get(i).getEmail());
     * ps.setInt(3, rsvp.get(i).getPhone());
     * ps.setDate(4, rsvp.get(i).getConfirmationDate());
     * ps.setString(5, rsvp.get(i).getComments());
     * }
     * 
     * public int getBatchSize() {
     * return rsvp.size();
     * }
     * 
     * 
     * });
     * }
     */
}
