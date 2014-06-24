package info.seltenheim.mate.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface MateService {

    public List<MateJunky> findAllJunkies() throws IOException;

    public MateJunky findJunkyByName(String name) throws IOException;

    public MateJunky addJunky(String name) throws IOException;

    public int getTotalBottleCount() throws IOException;

    /**
     * 
     * @param junkyId
     * @return new Count
     * @throws IOException 
     */
    public int countMate(String name) throws IOException;

    /**
     * 
     * @param name
     * @param amount
     * @return new remaining
     * @throws IOException 
     */
    public int addRemainingBottles(String name, int amount) throws IOException;

}
