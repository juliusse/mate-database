package info.seltenheim.mate.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface MateService {

    public List<MateJunky> findAllJunkies() throws IOException;

    public MateJunky findJunkyByName(String name) throws IOException;

    public MateJunky addJunky(String name) throws IOException;

    public MateJunky updateJunky(MateJunky junky);

    public boolean removeJunky(String name);

    public int getTotalBottleCount();

    /**
     * 
     * @param junkyId
     * @return new Count
     */
    public int countMate(String name);
}
