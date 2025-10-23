package com.thms.dataloader;

import com.thms.model.*;
import com.thms.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class LocationDataSeeder implements ApplicationRunner {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final SectorRepository sectorRepository;
    private final CellRepository cellRepository;
    private final VillageRepository villageRepository;

    public LocationDataSeeder(ProvinceRepository provinceRepository,
                              DistrictRepository districtRepository,
                              SectorRepository sectorRepository,
                              CellRepository cellRepository,
                              VillageRepository villageRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.sectorRepository = sectorRepository;
        this.cellRepository = cellRepository;
        this.villageRepository = villageRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Starting Location Data Seeding...");

        try {
            // Use reflection-based helpers so we don't rely on Lombok-generated setters/getters
            Object p1 = findOrCreateProvince("01", "Kigali");

            Object d1 = findOrCreateDistrict("0101", "Gasabo", p1);
            Object s1 = findOrCreateSector("010101", "Kacyiru", d1);
            Object c1 = findOrCreateCell("01010101", "Kimironko", s1);
            findOrCreateVillage("0101010101", "Kimironko A", c1);

            Object d2 = findOrCreateDistrict("0102", "Nyarugenge", p1);
            Object s2 = findOrCreateSector("010201", "Nyamirambo", d2);
            Object c2 = findOrCreateCell("01020101", "Nyamirambo A", s2);
            findOrCreateVillage("0102010101", "Nyamirambo I", c2);

            Object p2 = findOrCreateProvince("02", "Southern Province");
            Object sd1 = findOrCreateDistrict("0201", "Huye", p2);
            Object ss1 = findOrCreateSector("020101", "Tumba", sd1);
            Object sc1 = findOrCreateCell("02010101", "Tumba A", ss1);
            findOrCreateVillage("0201010101", "Tumba Village 1", sc1);

            System.out.println("Location Data Seeding completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during location data seeding: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Reflection helpers that avoid direct calls to setters/getters or repository custom methods.

    private Object findOrCreateProvince(String code, String name) throws Exception {
        Object existing = findByCodeReflectively(provinceRepository, code);
        if (existing != null) {
            System.out.println("Province already exists: " + name);
            return existing;
        }

        Object province = newInstance("com.thms.model.Province");
        setProperty(province, "code", code);
        setProperty(province, "name", name);
        Object saved = saveReflectively(provinceRepository, province);
        System.out.println("Created province: " + name);
        return saved;
    }

    private Object findOrCreateDistrict(String code, String name, Object province) throws Exception {
        Object existing = findByCodeReflectively(districtRepository, code);
        if (existing != null) {
            System.out.println("District already exists: " + name);
            return existing;
        }

        Object district = newInstance("com.thms.model.District");
        setProperty(district, "code", code);
        setProperty(district, "name", name);
        setProperty(district, "province", province);
        Object saved = saveReflectively(districtRepository, district);
        System.out.println("Created district: " + name);
        return saved;
    }

    private Object findOrCreateSector(String code, String name, Object district) throws Exception {
        Object existing = findByCodeReflectively(sectorRepository, code);
        if (existing != null) {
            System.out.println("Sector already exists: " + name);
            return existing;
        }

        Object sector = newInstance("com.thms.model.Sector");
        setProperty(sector, "code", code);
        setProperty(sector, "name", name);
        setProperty(sector, "district", district);
        Object saved = saveReflectively(sectorRepository, sector);
        System.out.println("Created sector: " + name);
        return saved;
    }

    private Object findOrCreateCell(String code, String name, Object sector) throws Exception {
        Object existing = findByCodeReflectively(cellRepository, code);
        if (existing != null) {
            System.out.println("Cell already exists: " + name);
            return existing;
        }

        Object cell = newInstance("com.thms.model.Cell");
        setProperty(cell, "code", code);
        setProperty(cell, "name", name);
        setProperty(cell, "sector", sector);
        Object saved = saveReflectively(cellRepository, cell);
        System.out.println("Created cell: " + name);
        return saved;
    }

    private Object findOrCreateVillage(String code, String name, Object cell) throws Exception {
        Object existing = findByCodeReflectively(villageRepository, code);
        if (existing != null) {
            System.out.println("Village already exists: " + name);
            return existing;
        }

        Object village = newInstance("com.thms.model.Village");
        setProperty(village, "code", code);
        setProperty(village, "name", name);
        setProperty(village, "cell", cell);
        Object saved = saveReflectively(villageRepository, village);
        System.out.println("Created village: " + name);
        return saved;
    }

    private Object findByCodeReflectively(Object repository, String code) {
        if (repository == null || code == null) return null;
        try {
            // Try repository.findByCode(String)
            Method m = repository.getClass().getMethod("findByCode", String.class);
            Object result = m.invoke(repository, code);
            if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                return opt.orElse(null);
            }
            return result;
        } catch (NoSuchMethodException ex) {
            // Fallback to findAll and search by code property
            try {
                Method mAll = repository.getClass().getMethod("findAll");
                Object all = mAll.invoke(repository);
                if (all instanceof Iterable) {
                    for (Object item : (Iterable<?>) all) {
                        Object val = getProperty(item, "code");
                        if (val != null && code.equals(String.valueOf(val))) {
                            return item;
                        }
                    }
                }
            } catch (Exception e) {
                // ignore and return null
            }
        } catch (Exception e) {
            // ignore and return null
        }
        return null;
    }

    private Object saveReflectively(Object repository, Object entity) throws Exception {
        if (repository == null) throw new IllegalArgumentException("repository is null");
        // find any "save" method with a single parameter
        for (Method m : repository.getClass().getMethods()) {
            if ("save".equalsIgnoreCase(m.getName()) && m.getParameterCount() == 1) {
                return m.invoke(repository, entity);
            }
        }
        // fallback to throw
        throw new NoSuchMethodException("No save method found on repository: " + repository.getClass());
    }

    private Object newInstance(String fqcn) throws Exception {
        Class<?> cls = Class.forName(fqcn);
        return cls.getDeclaredConstructor().newInstance();
    }

    private void setProperty(Object target, String prop, Object value) {
        if (target == null || prop == null || prop.isEmpty()) return;
        String setter = "set" + Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
        try {
            Method m = findMethodIgnoreCase(target.getClass(), setter, value == null ? new Class<?>[]{Object.class} : new Class<?>[]{value.getClass()});
            if (m != null) {
                m.invoke(target, value);
                return;
            }
        } catch (Exception e) {
            // fallback to field set
        }
        try {
            Field f = findFieldIgnoreCase(target.getClass(), prop);
            if (f != null) {
                f.setAccessible(true);
                f.set(target, value);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private Object getProperty(Object target, String prop) {
        if (target == null || prop == null || prop.isEmpty()) return null;
        String getter = "get" + Character.toUpperCase(prop.charAt(0)) + prop.substring(1);
        try {
            Method m = findMethodIgnoreCase(target.getClass(), getter);
            if (m != null) {
                return m.invoke(target);
            }
        } catch (Exception e) {
            // fallback to field access
        }
        try {
            Field f = findFieldIgnoreCase(target.getClass(), prop);
            if (f != null) {
                f.setAccessible(true);
                return f.get(target);
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    private Method findMethodIgnoreCase(Class<?> cls, String name, Class<?>... params) {
        if (cls == null || name == null) return null;
        for (Method m : cls.getMethods()) {
            if (m.getName().equalsIgnoreCase(name) && (params == null || params.length == 0 || parametersCompatible(m.getParameterTypes(), params))) {
                return m;
            }
        }
        return null;
    }

    private Field findFieldIgnoreCase(Class<?> cls, String name) {
        Class<?> cur = cls;
        while (cur != null) {
            for (Field f : cur.getDeclaredFields()) {
                if (f.getName().equalsIgnoreCase(name)) return f;
            }
            cur = cur.getSuperclass();
        }
        return null;
    }

    private boolean parametersCompatible(Class<?>[] methodParams, Class<?>[] wanted) {
        if (methodParams == null || wanted == null) return false;
        if (methodParams.length != wanted.length) return false;
        for (int i = 0; i < methodParams.length; i++) {
            if (wanted[i] == null) continue;
            if (!methodParams[i].isAssignableFrom(wanted[i]) && !wanted[i].equals(Object.class)) return false;
        }
        return true;
    }
}
