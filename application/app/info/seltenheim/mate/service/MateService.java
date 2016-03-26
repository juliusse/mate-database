package info.seltenheim.mate.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public interface MateService {

    public List<MateJunky> findAllJunkies() throws IOException;

    public MateJunky findJunkyById(int id) throws IOException;

    public MateJunky findJunkyByName(String name) throws IOException;

    public MateJunky addJunky(String name) throws IOException;

    public boolean updateJunky(MateJunky junky) throws IOException;

    public JsonNode getMetaInformationAsJson() throws IOException;

    public void addMate(int count) throws IOException;

    public List<MateLogEntry> getAllLogEntries() throws IOException;

    public void setCurrentBottlePrice(double newPricePerBottle) throws IOException;

    public void drinkMate(MateJunky junky) throws IOException;

    public void deleteJunkie(int id) throws IOException;
}
