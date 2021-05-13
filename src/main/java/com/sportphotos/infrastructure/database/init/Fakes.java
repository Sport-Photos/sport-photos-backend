package com.sportphotos.infrastructure.database.init;

import static com.google.common.base.Preconditions.checkArgument;

import com.github.javafaker.Faker;
import com.sportphotos.domain.events.model.Event;
import com.sportphotos.domain.events.model.Location;
import com.sportphotos.domain.events.model.PhotoCoverage;
import com.sportphotos.domain.photographers.model.Photographer;
import com.sportphotos.domain.photographers.model.Rating;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Fakes {

  private static final SecureRandom random = new SecureRandom();
  private static final Faker faker = new Faker(random);
  private static int nicknameCounter = 10000;
  private static int eventNameCounter = 10000;

  static Event fakeEvent(Supplier<List<PhotoCoverage>> photoCoverages) {
    var event =
        Event.builder()
            .id(UUID.randomUUID().toString())
            .date(
                faker
                    .date()
                    .past(1000, TimeUnit.DAYS)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate())
            .name(faker.lordOfTheRings().location() + " - " + eventNameCounter++)
            .photoCoverages(photoCoverages.get())
            .avatar(fakePhoto(400, 300))
            .location(new Location(faker.starTrek().location(), faker.country().name()))
            .build();

    log.info("Event - [{}] - created ", event.getId());

    return event;
  }

  static List<Event> fakeEvents(
      int from,
      int to,
      int photoCoveragesFrom,
      int photoCoveragesTo,
      List<Photographer> photographers) {

    return fakeCollection(
        from,
        to,
        () ->
            fakeEvent(
                () -> fakePhotoCoverages(photoCoveragesFrom, photoCoveragesTo, photographers)));
  }

  static PhotoCoverage fakePhotoCoverage(Photographer photographer) {
    var photoCoverage =
        PhotoCoverage.builder()
            .id(UUID.randomUUID().toString())
            .description(faker.chuckNorris().fact())
            .link(faker.internet().url())
            .bestPhoto(fakePhoto(300, 200))
            .photographer(photographer)
            .build();

    log.info("Photo Coverage - [{}] - created ", photoCoverage.getId());

    return photoCoverage;
  }

  static List<PhotoCoverage> fakePhotoCoverages(
      int from, int to, List<Photographer> photographers) {
    return fakeCollection(from, to, () -> fakePhotoCoverage(choose(photographers)));
  }

  static Photographer fakePhotographer(Supplier<List<Rating>> ratings) {
    var photographer =
        Photographer.builder()
            .id(UUID.randomUUID().toString())
            .nickname(faker.gameOfThrones().character() + " - " + nicknameCounter++)
            .ratings(ratings.get())
            .build();

    log.info("Photographer - [{}] - created ", photographer.getId());

    return photographer;
  }

  static List<Photographer> fakePhotographers(int from, int to, int ratingsFrom, int ratingsTo) {
    return fakeCollection(
        from, to, () -> fakePhotographer(() -> fakeRatings(ratingsFrom, ratingsTo)));
  }

  static Rating fakeRating() {
    var rating =
        Rating.builder()
            .id(UUID.randomUUID().toString())
            .rate(faker.number().numberBetween(1, 10))
            .comment(faker.shakespeare().hamletQuote())
            .build();

    log.info("Rating - [{}] - created ", rating.getId());

    return rating;
  }

  static List<Rating> fakeRatings(int from, int to) {
    return fakeCollection(from, to, Fakes::fakeRating);
  }

  static <T> List<T> fakeCollection(int from, int to, Supplier<T> supplier) {
    checkArgument(from <= to, "range from is greater than range to");

    return IntStream.range(0, from + randomInt(to - from + 1))
        .mapToObj(ignored -> supplier.get())
        .collect(Collectors.toList());
  }

  @SneakyThrows
  static Binary fakePhoto(int width, int height) {
    var client =
        HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    var request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://picsum.photos/" + width + "/" + height))
            .build();

    return new Binary(
        BsonBinarySubType.BINARY,
        client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body().readAllBytes());
  }

  private static int randomInt(int num) {
    return random.nextInt(num);
  }

  private static <T> T choose(List<T> objects) {
    return objects.get(randomInt(objects.size()));
  }
}
