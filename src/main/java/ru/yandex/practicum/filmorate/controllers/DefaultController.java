package ru.yandex.practicum.filmorate.controllers;

        import lombok.extern.slf4j.Slf4j;
        import org.springframework.web.bind.annotation.*;
        import ru.yandex.practicum.filmorate.exceptions.DataDoesNotExistsException;
        import ru.yandex.practicum.filmorate.models.DefaultModel;

        import javax.validation.Valid;
        import java.util.Collection;
        import java.util.HashMap;

@Slf4j
@RestController
public abstract class DefaultController<T extends DefaultModel> {

    HashMap<Integer, T> data = new HashMap<>();

    @GetMapping()
    public Collection<T> GetData() {
        log.info("/GET");
        return data.values();
    }

    @PostMapping
    public T createData(@Valid @RequestBody T element) {
        log.info("/POST: " + element.toString());
        isValid(element);
        element.setId(data.size() + 1);
        data.put(element.getId(), element);
        return element;
    }

    @PutMapping
    public T updateData (@Valid @RequestBody T element) {
        log.info("/PUT: " + element.toString());
        isValid(element);
        if(element.getId() != null) {
            if(data.containsKey(element.getId())) {
                data.put(element.getId(), element);
                log.info("Data info has been updated");
            } else {
                log.warn("\"Data with id " + element.getId() + " doesn't exist");
                throw new DataDoesNotExistsException("Data with this id doesn't exist");
            }
        } else {
            element.setId(data.size() + 1);
            data.put(element.getId(), element);
            log.info("New data has been added: " + element.toString());
        }
        return element;
    }
    /*получается если сделать этот метод тут, то он будет протектед, потому что только в наследниках он будет
     использоваться, и тогда у нему не нужны тесты*/
    protected abstract boolean isValid(T element);

}
