package ru.geekbrains.lesson5;

import java.util.*;

public class Editor3D implements UILayer {

    private ProjectFile projectFile;
    private BusinessLogicalLayer businessLogicalLayer;
    private DatabaseAccess databaseAccess;
    private Database database;
    private ArrayList<Model3D> models;
    private ArrayList<Texture> textures;

    public Editor3D() {
    }

    private void initialize() {
        database = new EditorDatabase(projectFile);
        databaseAccess = new EditorDatabaseAccess(database);
        businessLogicalLayer = new EditorBusinessLogicalLayer(databaseAccess);
        textures = (ArrayList<Texture>) businessLogicalLayer.getAllTextures();
        models = (ArrayList<Model3D>) businessLogicalLayer.getAllModels();
    }
    @Override
    public void openProject(String fileName) {
        projectFile = new ProjectFile(fileName);
        initialize();
    }

    @Override
    public void showProjectSettings() {

        System.out.println("*** Project v1 ***");
        System.out.println("******************");
        System.out.printf("fileName: %s\n", projectFile.getFileName());
        System.out.printf("setting1: %d\n", projectFile.getSetting1());
        System.out.printf("setting2: %s\n", projectFile.getSetting2());
        System.out.printf("setting3: %s\n", projectFile.getSetting3());
        System.out.println("******************");
    }

    @Override
    public void saveProject() {
        System.out.println("Изменения успешно сохранены.");
        database.save();
    }

    @Override
    public void printAllModels() {
        for (int i = 0; i < models.size(); i++) {
            System.out.printf("===%d===\n", i);
            System.out.println(models.get(i));
            for (Texture texture : models.get(i).getTextures()) {
                System.out.printf("\t%s\n", texture);
            }
        }
    }

    @Override
    public void printAllTextures() {
        for (int i = 0; i < textures.size(); i++) {
            System.out.printf("===%d===\n", i);
            System.out.println(textures.get(i));
        }
    }

    @Override
    public void renderAll() {
        System.out.println("Подождите ...");
        long startTime = System.currentTimeMillis();
        businessLogicalLayer.renderAllModels();
        long endTime = (System.currentTimeMillis() - startTime);
        System.out.printf("Операция выполнена за %d мс.\n", endTime);
    }

    @Override
    public void renderModel(int i) {
        if (i < 0 || i > models.size() - 1)
            throw new RuntimeException("Номер модели указан некорректною.");
        System.out.println("Подождите ...");
        long startTime = System.currentTimeMillis();
        businessLogicalLayer.renderModel(models.get(i));
        long endTime = (System.currentTimeMillis() - startTime);
        System.out.printf("Операция выполнена за %d мс.\n", endTime);
    }

    @Override
    public void delModel(int i) {
        if (i < 0 || i > models.size())
            throw new RuntimeException("Номер модели указан некорректно.");
        // не знаю, красиво ли получилось, но все работает:)
        models.get(i).getTextures()
                .forEach(x -> textures.removeIf(t ->t.getId()==x.getId()));
        models.remove(i);
        System.out.println("Деталь удалена!");
    }

    @Override
    public void delTexture(int i) {
        if (i < 0 || i > textures.size())
            throw new RuntimeException("Номер тестуры указан некорректно.");
        models.forEach(m -> m.getTextures().
                        removeIf(t ->t.getId()==textures.get(i).getId()));
        textures.remove(i);
        System.out.println("Текстура удалена!");
    }
}