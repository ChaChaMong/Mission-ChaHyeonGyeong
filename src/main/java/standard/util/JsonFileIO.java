package standard.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonFileIO<T> {
// Gson 객체 생성
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<T> readFile(String filePath, Class<T> type) {
        List<T> dataList = null;

        createFile(filePath);

        try (FileReader reader = new FileReader(filePath)) {
            // JSON 파일을 읽어서 List로 파싱
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            dataList = gson.fromJson(reader, listType); // List 타입으로 파싱
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void writeFile(List<T> dataList, String filePath) {
        String jsonData = gson.toJson(dataList);

        try (FileWriter writer = new FileWriter(filePath)) {
            // JSON 문자열을 파일에 쓰기
            writer.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile(String filePath) {

        File file = new File(filePath); // 파일 객체 생성

        if (!file.exists()) {
            try {
                // 파일이 존재하지 않을 경우, 새로운 파일 생성
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
