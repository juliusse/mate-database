package info.seltenheim.mate.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface MateService {

    public List<MateJunky> findAllJunkies() throws IOException;

    public MateJunky findJunkyById(int id) throws IOException;

    public MateJunky findJunkyByName(String name) throws IOException;

    public MateJunky addJunky(String name) throws IOException;

    public boolean updateJunky(MateJunky junky) throws IOException;

    public List<MateLogEntry> getAllLogEntries() throws IOException;

    /**
     * 
     * @return price in euro
     * @throws IOException
     */
    public double getCurrentBottlePrice() throws IOException;
}
