import translations from 'ckeditor5/translations/ko.js';
import {
  AutoImage,
  Autosave,
  Bold,
  EditorConfig,
  Essentials,
  FileRepository,
  FontBackgroundColor,
  FontColor,
  FontSize,
  GeneralHtmlSupport,
  ImageBlock,
  ImageInline,
  ImageInsert,
  ImageInsertViaUrl,
  ImageResize,
  ImageToolbar,
  ImageUpload,
  Italic,
  Link,
  LinkImage,
  Paragraph,
  SelectAll,
  SpecialCharacters,
  SpecialCharactersArrows,
  SpecialCharactersCurrency,
  SpecialCharactersEssentials,
  SpecialCharactersLatin,
  SpecialCharactersMathematical,
  SpecialCharactersText,
  Table,
  TableToolbar,
  Underline,
  Undo,
} from 'ckeditor5';

const editorConfig: EditorConfig = {
  toolbar: {
    items: [
      'undo',
      'redo',
      '|',
      'selectAll',
      '|',
      'fontSize',
      'fontColor',
      'fontBackgroundColor',
      '|',
      'bold',
      'italic',
      'underline',
      '|',
      'specialCharacters',
      'link',
      'insertImage',
      'insertTable',
      '|',
    ],
    shouldNotGroupWhenFull: false,
  },
  plugins: [
    AutoImage,
    Autosave,
    Bold,
    Essentials,
    FileRepository,
    FontBackgroundColor,
    FontColor,
    FontSize,
    GeneralHtmlSupport,
    ImageBlock,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageToolbar,
    ImageUpload,
    Italic,
    Link,
    LinkImage,
    Paragraph,
    SelectAll,
    SpecialCharacters,
    SpecialCharactersArrows,
    SpecialCharactersCurrency,
    SpecialCharactersEssentials,
    SpecialCharactersLatin,
    SpecialCharactersMathematical,
    SpecialCharactersText,
    Table,
    TableToolbar,
    Underline,
    Undo,
  ],
  fontFamily: {
    supportAllValues: true,
  },
  fontSize: {
    options: [10, 12, 14, 'default', 18, 20, 22],
    supportAllValues: true,
  },
  image: {
    toolbar: ['imageTextAlternative', '|', 'resizeImage'],
  },
  htmlSupport: {
    allow: [
      {
        name: /^.*$/,
        styles: true,
        attributes: true,
        classes: true,
      },
    ],
  },
  link: {
    addTargetToExternalLinks: true,
    defaultProtocol: 'https://',
    decorators: {
      toggleDownloadable: {
        mode: 'manual',
        label: 'Downloadable',
        attributes: {
          download: 'file',
        },
      },
    },
  },
  table: {
    contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells'],
  },
  initialData: '',
  language: 'ko',
  placeholder: '본문을 입력하세요',
  translations: [translations],
};
export default editorConfig;
