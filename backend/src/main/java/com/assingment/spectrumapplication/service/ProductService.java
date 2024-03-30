package com.assingment.spectrumapplication.service;


import com.assingment.spectrumapplication.dao.ProductRepository;
import com.assingment.spectrumapplication.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional
    public void saveDataFromFile(MultipartFile file){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String lines;
            reader.readLine(); // Skipping the header line
            while((lines = reader.readLine()) != null){
                String[] chunks = lines.split("[,:]");
                if(chunks.length == 4){
                    Product product = new Product();
                    product.setId(Long.parseLong(chunks[0].trim()));
                    product.setProductName(chunks[1].trim());
                    product.setVoucherNo(chunks[2].trim());
                    product.setPrice(Float.parseFloat(chunks[3].trim()));

                    productRepository.save(product);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to save data from file: " + ex.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public byte[] generateCSV() {
        List<Product> products = productRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("ProductId,productName,voucherNo,Price");
        for (Product product : products) {
            writer.println(product.getId() + "," + product.getProductName() + ","
                    + product.getVoucherNo() + "," + product.getPrice());
        }
        writer.flush();
        writer.close();
        return outputStream.toByteArray();
    }


}
