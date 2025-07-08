import java.util.HashMap;
import java.util.Map;

public class FlagData {
  private static final Map<String, String> FLAG_TO_COUNTRY_MAP = new HashMap<>();

  static {
    // You MUST populate this map with actual image URLs and correct country names.
    // Get the image source from the website for each flag, and map it to the correct country.
    // Example (replace with actual data from the quiz):
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/017638a4-1d10-4c6b-8453-25aef4b8ac53.jpg", "Japan");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/8fe5a6ac-7d69-423a-b07e-3f1d33914aba.jpg", "Georgien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/6e0c0037-92f6-47e7-84b4-5a396bc8823d.jpg", "Filippinerne");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/83f02de0-10cd-4d26-9271-7448978c8361.jpg", "Sydkorea");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/6ed9acf5-2875-45dc-8afe-b56cc2935af4.jpg", "Vietnam");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/643c071c-1619-4920-9b04-7c543486f15b.jpg", "Aserbajdsjan");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/cd61cb3d-fba0-4e99-b589-a0a6ba282639.jpg", "Argentina");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/f5b3a701-c812-4bf0-8b7a-f899c917998b.jpg", "Brasilien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/faae75a7-6304-41bd-a1f8-098cb5829906.jpg", "Thailand");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/1acd49fb-5ed9-456e-aae4-dc8d85d9ef85.jpg", "Letland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/4b309606-de26-4b4b-9cb2-09dfa35abb75.jpg", "Canada");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/5b29024f-8675-46b7-bc84-8b438171883d.jpg", "New Zealand");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/aecf303f-42d5-4f0f-95a5-7ed2597ba6a2.jpg", "Belgien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/9247862d-1ff5-42f2-863f-d757f3f2423e.jpg", "Tyrkiet");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/f5b96932-4458-483a-976a-fc857444a336.jpg", "Kroatien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/26323ad9-37d5-453a-956c-4ddba57dc406.jpg", "Hongkong");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/24f291ea-647f-4ae6-ad80-c2ca30af7222.jpg", "Estland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/0633a3f5-80cd-420a-94cf-c5d5e99a7a49.jpg", "Schweiz");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/12b767ed-67f4-4a35-b34d-b200dbfb68e3.jpg", "Indien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/bc910225-5f36-41fe-8aec-0cc32641ceb7.jpg", "Montenegro");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/6ab3c860-797e-4b78-b8d9-ebe45dfdb843.jpg", "Luxembourg");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/74665214-fec5-48b9-87ca-823584f4c1e9.jpg", "Cypern");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/e188f84d-19bc-4c70-b886-3b78d0c15bd8.jpg", "Bulgarien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/cb20cccb-ab6d-4d25-a49a-ef2340becb18.jpg", "Ungarn");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/00604769-c5dc-44a8-8d4b-fb351a5c14cc.jpg", "Grønland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/ea0bb468-ea0c-4c88-af09-92442b66cc78.jpg", "Slovenien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/8e284847-2593-4cad-9395-0348c09e7e11.jpg", "Jamaica");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/a5b01c16-ea35-4ef5-bea0-a21502a292fd.jpg", "Grækenland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/a5151667-59c6-4192-be16-77c84bf4172e.jpg", "Afghanistan");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/0dee2f67-f950-4c31-83f3-438fb10d4834.jpg", "Irland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/2dd248c1-29c0-44f4-b05d-66cf3ac473dd.jpg", "USA");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/07ce269f-c8ba-4bae-8c73-178891e71698.jpg", "Spanien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/4d66212f-d093-44c1-b810-a3fec7dce42c.jpg", "Danmark");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/74eb85a4-fda0-4b18-b1b5-47dad6f1012f.jpg", "Egypten");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/b4c7b34d-d659-4c75-95d1-16fdf3b4ec33.jpg", "Kenya");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/08678aa6-2932-4134-a97e-31ca02d2181d.jpg", "Ukraine");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/be6ce4ee-a721-4353-8cf5-1098efe6ebb0.jpg", "Italien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/81e12fe3-bd47-4a46-8882-da1bf4df5d08.jpg", "Storbritannien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/fe98c6b9-7f0b-49a0-8fab-75ef96721bf6.jpg", "Kina");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/28ea038f-e801-4ae4-9aa8-b7e5ee014ad2.jpg", "Polen");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/68397d9a-33b5-4690-b05f-0ada126808e2.jpg", "Island");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/66c3d451-5c80-47f0-a366-406b292da0fb.jpg", "Nepal");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/4a4437cd-2735-4c84-b8b2-d7fdc5838d93.jpg", "Østrig");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/4af95057-753c-4b05-a4cf-de580f8b1474.jpg", "Litauen");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/0d4ad008-afd6-46d6-9041-d10c03a9a89e.jpg", "Cuba");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/ff7a8c47-d0bd-444f-8681-6d77df861cc4.jpg", "Albanien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/91682200-8e01-43e2-a13e-324745f80566.jpg", "Rusland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/16370fe9-a102-4704-9bfd-c21997a08296.jpg", "Mexico");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/7e285110-ad18-44d8-8d87-1ea2f7ad1a01.jpg", "Sydafrika");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/fb80f767-517b-478e-8b2b-11254d3d0794.jpg", "Færøerne");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/896676e8-5d67-4013-9f35-9b00b1b64b2a.jpg", "Sverige");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/237c3b5c-131b-4671-9f33-5cae7fd89a0e.jpg", "Frankrig");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/4fe2cc47-2c1c-4edb-ba49-3f0d97d43422.jpg", "Portugal");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/70c3fc70-3786-4e35-9bb4-ae3c56a54e47.jpg", "Finland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/fd873ead-25da-4a4d-9b39-b6b52b53b12b.jpg", "Israel");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/d43049b5-8468-43e5-9b22-73639c9c56ba.jpg", "Qatar");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/885126aa-5c24-477c-93ad-d284e9d69c95.jpg", "Malta");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/8836524d-fa4e-4fe3-b31f-4948980ab3ff.jpg", "Tyskland");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/e6ea6bb7-104a-435b-b511-b6f09722076a.jpg", "Australien");
    FLAG_TO_COUNTRY_MAP.put("/files/1480/uploads/4a751956-0b44-48e9-9ffd-8a6b41fb80e5.jpg", "Palæstina");
  }

  public static String getCountryForFlagUrl(String flagImageUrl) {
    // You might need to extract just the unique part of the URL if the base URL changes
    String uniquePart = extractUniquePart(flagImageUrl); // Implement this helper method
    return FLAG_TO_COUNTRY_MAP.get(uniquePart);
  }

  private static String extractUniquePart(String fullUrl) {
    // This is crucial. Determine what part of the URL is consistent and unique for each flag.
    // Based on the provided HTML, it looks like "/files/1480/uploads/..." is the unique part.
    // So, if the full URL is "https://quiz.jyllands-posten.dk/files/1480/uploads/07ce269f-c8ba-4bae-8c73-178891e71698.jpg"
    // you want to extract "/files/1480/uploads/07ce269f-c8ba-4bae-8c73-178891e71698.jpg"
    int startIndex = fullUrl.indexOf("/files/");
    if (startIndex != -1) {
      return fullUrl.substring(startIndex);
    }
    return fullUrl; // Fallback, might not be unique enough
  }
}