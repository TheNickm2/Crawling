package me.arthed.crawling.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;

import me.arthed.crawling.Crawling;
import org.bukkit.configuration.file.YamlConfiguration;

public class CrawlingConfig extends YamlConfiguration {

    private final File configFile;
    private Map<String, Object> data;

    public CrawlingConfig(String fileName) {
        super();

        Crawling plugin = Crawling.getInstance();
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
        reload();

        InputStream defaultConfigInputStream = Crawling.class.getResourceAsStream("/" + fileName);
        InputStreamReader defaultConfigReader = new InputStreamReader(defaultConfigInputStream);
        setDefaults(YamlConfiguration.loadConfiguration(defaultConfigReader));

    }

    public void reload() {
        try {
            load(configFile);
            data = new HashMap<>();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public List<Material> getMaterialList(String path) {
        if(data.containsKey(path))
            return (List<Material>)data.get(path);

        List<Material> result = new ArrayList<>();
        for(String materialName : getStringList(path)) {
            try {
                result.add(Material.valueOf(materialName));
            } catch(IllegalArgumentException ignore) {}
        }
        data.put(path, result);
        return result;
    }

    public List<World> getWorldList(String path) {
        if(data.containsKey(path))
            return (List<World>)data.get(path);

        List<World> result = new ArrayList<>();
        for(String worldName : getStringList(path)) {
            World world = Bukkit.getWorld(worldName);
            if(world != null)
                result.add(world);
        }
        data.put(path, result);
        return result;
    }

}
