package com.noteapp.model.datatransfer;

import com.noteapp.model.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author admin
 */
public class NoteBlock extends BaseDataTransferModel {
    private int ord;
    private String header;
    private String editor;
    private String content;
    private BlockType blockType;
    
    public static enum BlockType {
        TEXT, IMAGE, SURVEY
    }
    
    public NoteBlock() {
        this.ord = -1;
        this.header = "";
        this.editor = "";
        this.content = "";
        this.blockType = BlockType.TEXT;
    }

    public NoteBlock(int ord, String header, String editor, String content, BlockType blockType) {
        this.ord = ord;
        this.header = header;
        this.editor = editor;
        this.content = content;
        this.blockType = blockType;
    }
    
    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }
    
    /**
     * Kiểm tra xem một thể hiện Note có mang giá trị default không
     * @return (1) {@code true} nếu đây là default Note, (2) {@code false} nếu ngược lại
     */
    @Override
    public boolean isDefaultValue() {
        return ord == -1;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.ord;
        hash = 89 * hash + Objects.hashCode(this.header);
        hash = 89 * hash + Objects.hashCode(this.editor);
        hash = 89 * hash + Objects.hashCode(this.content);
        hash = 89 * hash + Objects.hashCode(this.blockType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NoteBlock other = (NoteBlock) obj;
        if (this.ord != other.ord) {
            return false;
        }
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.editor, other.editor)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return this.blockType == other.blockType;
    }
    
    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("id", this.ord);
        attributeMap.put("header", this.header);
        attributeMap.put("editor", this.editor);
        attributeMap.put("content", this.content);
        attributeMap.put("blockType", this.blockType);
        return attributeMap;
    }

    @Override
    public String toString() {
        String objectName = "Block";
        return super.toString(objectName);
    }
    
    public static NoteBlock valueOf(String str) {
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static NoteBlock valueOf(Map<String, String> attributeStrMap) {
        NoteBlock block = new NoteBlock();
        block.setOrd(Integer.parseInt(attributeStrMap.get("id")));
        block.setHeader(attributeStrMap.get("header"));
        block.setEditor(attributeStrMap.get("editor"));
        block.setContent(attributeStrMap.get("content"));
        block.setBlockType(BlockType.valueOf(attributeStrMap.get("blockType")));
        return block;
    }
    
    public static class ListConverter {
        public static String convertToString(List<? extends NoteBlock> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<NoteBlock> convertToList(String listOfBlockStr) {
            List<Map<String, String>> listOfBlockMaps = Command.decode(listOfBlockStr);
            List<NoteBlock> blocks = new ArrayList<>();
            for(Map<String, String> blockMap: listOfBlockMaps) {
                NoteBlock newBlock = NoteBlock.valueOf(blockMap);
                blocks.add(newBlock);
            }
            return blocks;
        }
    }
    
        /**
     * Chuyển một text hiển thị trên GUI sang một text lưu trong CSDL và ngược lại
     */
    public static class TextContentConverter {
        private static final String ENDLINE_TAGS = "##endline##";

        /**
         * Chuyển một text ở GUI sang text lưu trong CSDL
         * @param areaText text ở GUI
         * @return text sau khi chuyển
         */
        public static String convertToDBText(String areaText) {
            //Chia thành các dòng
            String[] texts = areaText.split("\\n");
            String dbText = "";
            //Chuyển ký tự \n ở cuối dòng thành ##endline##
            for (String text : texts) {
                dbText += text + ENDLINE_TAGS;
            }
            return dbText;
        }

        /**
         * Chuyển một text trong CSDL sang một text hiển thị trên GUI
         * @param dbText text trong CSDL
         * @return text sau khi chuyển
         */
        public static String convertToObjectText(String dbText) {
            //Chia thành các phần ngăn cách bởi ##endline##, mỗi phần là một dòng trên text ở GUI
            String[] texts = dbText.split(ENDLINE_TAGS);
            String areaText = "";
            //Thêm ký tự \n vào cuối mỗi phần tử
            for (String text : texts) {
                areaText += text + "\n";
            }
            return areaText;
        }
    }
}