package ryhma5.model.api.astronomyAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Static class to convert between constellation names and their ids.
 */
public class ConstellationConverter {

    private static final Map<String, String> nameToIdMap = new HashMap<>();
    private static final Map<String, String> idToNameMap = new HashMap<>();

    static {
        initializeMaps();
    }

    // Initialize the maps with the constellation names and their ids
    private static void initializeMaps() {
        addMapping("Andromeda", "and");
        addMapping("Leo", "leo");
        addMapping("Antlia", "ant");
        addMapping("Leo Minor", "lmi");
        addMapping("Apus", "aps");
        addMapping("Lepus", "lep");
        addMapping("Aquarius", "aqr");
        addMapping("Libra", "lib");
        addMapping("Aquila", "aql");
        addMapping("Lupus", "lup");
        addMapping("Ara", "ara");
        addMapping("Lynx", "lyn");
        addMapping("Aries", "ari");
        addMapping("Lyra", "lyr");
        addMapping("Auriga", "aur");
        addMapping("Mensa", "men");
        addMapping("Boötes", "boo");
        addMapping("Microscopium", "mic");
        addMapping("Caelum", "cae");
        addMapping("Monoceros", "mon");
        addMapping("Camelopardalis", "cam");
        addMapping("Musca", "mus");
        addMapping("Cancer", "cnc");
        addMapping("Norma", "nor");
        addMapping("Canes Venatici", "cvn");
        addMapping("Octans", "oct");
        addMapping("Canis Major", "cma");
        addMapping("Ophiuchus", "oph");
        addMapping("Canis Minor", "cmi");
        addMapping("Orion", "ori");
        addMapping("Capricornus", "cap");
        addMapping("Pavo", "pav");
        addMapping("Carina", "car");
        addMapping("Pegasus", "peg");
        addMapping("Cassiopeia", "cas");
        addMapping("Perseus", "per");
        addMapping("Centaurus", "cen");
        addMapping("Phoenix", "phe");
        addMapping("Cepheus", "cep");
        addMapping("Pictor", "pic");
        addMapping("Cetus", "cet");
        addMapping("Pisces", "psc");
        addMapping("Chamaeleon", "cha");
        addMapping("Piscis Austrinus", "psa");
        addMapping("Circinus", "cir");
        addMapping("Puppis", "pup");
        addMapping("Columba", "col");
        addMapping("Pyxis", "pyx");
        addMapping("Coma Berenices", "com");
        addMapping("Reticulum", "ret");
        addMapping("Corona Australis", "cra");
        addMapping("Sagitta", "sge");
        addMapping("Corona Borealis", "crb");
        addMapping("Sagittarius", "sgr");
        addMapping("Corvus", "crv");
        addMapping("Scorpius", "sco");
        addMapping("Crater", "crt");
        addMapping("Sculptor", "scl");
        addMapping("Crux", "cru");
        addMapping("Scutum", "sct");
        addMapping("Cygnus", "cyg");
        addMapping("Serpens", "ser");
        addMapping("Delphinus", "del");
        addMapping("Dorado", "dor");
        addMapping("Sextans", "sex");
        addMapping("Draco", "dra");
        addMapping("Taurus", "tau");
        addMapping("Equuleus", "equ");
        addMapping("Telescopium", "tel");
        addMapping("Eridanus", "eri");
        addMapping("Triangulum", "tri");
        addMapping("Fornax", "for");
        addMapping("Triangulum Australe", "tra");
        addMapping("Gemini", "gem");
        addMapping("Tucana", "tuc");
        addMapping("Grus", "gru");
        addMapping("Ursa Major", "uma");
        addMapping("Hercules", "her");
        addMapping("Ursa Minor", "umi");
        addMapping("Horologium", "hor");
        addMapping("Vela", "vel");
        addMapping("Hydra", "hya");
        addMapping("Virgo", "vir");
        addMapping("Hydrus", "hyi");
        addMapping("Volans", "vol");
        addMapping("Indus", "ind");
        addMapping("Vulpecula", "vul");
        addMapping("Lacerta", "lac");
    }


    private static void addMapping(String fullName, String id) {
        nameToIdMap.put(fullName, id);
        idToNameMap.put(id, fullName);
    }

    /**
     * @param name
     * @return the id of the constellation with the given name
     */
    public static String getIdFromName(String name) {
        return nameToIdMap.getOrDefault(name, "Unknown");
    }

    /**
     * @param id
     * @return the name of the constellation with the given id
     */
    public static String getNameFromId(String id) {
        return idToNameMap.getOrDefault(id, "Unknown");
    }
}

