package thkoeln.coco.ad.parser;

import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.primitives.Command;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    public Command parseInput(String inputToParse) {
        String badCharacterRegEx = "[^a-z0-9-,\\[\\]()]";
        String machineCommandRegEx = "^\\[.*\\]$";
        String barrierCommandRegEx = "^\\({1}[0-9]{1,9},[0-9]{1,9}\\)-\\([0-9]{1,9},[0-9]{1,9}\\){1}$";
        if (!isStringMatchingRegEx(inputToParse, badCharacterRegEx)) {
            if (isStringMatchingRegEx(inputToParse, machineCommandRegEx)) {
                return parseValidMachineCommand(inputToParse);
            } else if (isStringMatchingRegEx(inputToParse, barrierCommandRegEx)) {
                return parseBarrierCreation(inputToParse);
            }
        } else throw new MiningMachineException("Found Illegal Characters In Command!");
        throw new MiningMachineException("No known command found!");
    }

    private Command parseBarrierCreation(String inputToParse) {
        return Command.generateEntryCommand(UUID.randomUUID());
    }

    private Command parseValidMachineCommand(String stringToCheck) {
        String[] tokens = stringToCheck.split("");
        String command = tokens[1] + tokens[2];

        String movementRegEx = "^\\[{1}no|ea|we|so{1},[0-9]{1,9}\\]{1}$";
        String uuidRegEx = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}";
        String transportRegEx = "^\\[{1}tr{1}," + uuidRegEx + "\\]{1}$";
        String entryRegEx = "^\\[{1}en{1}," + uuidRegEx + "\\]{1}$";

        if (isStringMatchingRegEx(stringToCheck, movementRegEx)) {
            Integer steps = Integer.parseInt(parseRestOfMachineCommand(tokens));
            return Command.generateMoveCommand(command, steps);
        } else if (isStringMatchingRegEx(stringToCheck, uuidRegEx)) {
            UUID targetedId = UUID.fromString(parseRestOfMachineCommand(tokens));
            if (isStringMatchingRegEx(stringToCheck, transportRegEx)) {
                return Command.generateTransportCommand(targetedId);
            } else if (isStringMatchingRegEx(stringToCheck, entryRegEx)) {
                return Command.generateEntryCommand(targetedId);
            }
        }
        throw new MiningMachineException("Command does not match syntax");
    }

    private String parseRestOfMachineCommand(String[] tokens) {
        StringBuilder restOfCommand = new StringBuilder();
        for (int i = 4; i < tokens.length - 1; i++) {
            restOfCommand.append(tokens[i]);
        }
        return restOfCommand.toString();
    }

    private boolean isStringMatchingRegEx(String stringToMatch, String regEx) {
        Pattern regex = Pattern.compile(regEx);
        Matcher matcher = regex.matcher(stringToMatch);
        return matcher.matches();
    }

}
