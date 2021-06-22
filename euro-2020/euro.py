import csv
import os
from datetime import date

GAME_RESULTS = {
    "1) Turkey vs Italy": "0-3",
    "2) Wales vs Switzerland": "1-1",
    "3) Denmark vs Finland": "0-1",
    "4) Belgium vs Russia": "3-0",
    "5) England vs Croatia": "1-0",
    "6) Austria vs North Macedonia": "3-1",
    "7) Netherlands vs Ukraine": "3-2",
    "8) Scotland vs Czech Republic": "0-2",
    "9) Poland vs Slovakia": "1-2",
    "10) Spain vs Sweden": "0-0",
    "11) 🇭🇺Hungary vs Portugal 🇵🇹 ": "0-3",
    "12) 🇫🇷 France vs Germany 🇩🇪": "1-0",
    "13) 🇫🇮Finland vs Russia 🇷🇺": "0-1",
    "14) 🇹🇷 Turkey vs Wales 🏴󠁧󠁢󠁷󠁬󠁳󠁿": "0-2",
    "15) 🇮🇹Italy vs Switzerland 🇨🇭": "3-0",
    "16) 🇺🇦 Ukraine vs North Macedonia 🇲🇰": "2-1",
    "17) 🇩🇰 Denmark vs Belgium 🇧🇪 ": "1-2",
    "18) 🇳🇱 Netherlands vs Austria 🇦🇺": "2-0",
    "19) 🇸🇪Sweden vs Slovakia 🇸🇰": "1-0",
    "20) 🇭🇷Croatia vs Czech Republic 🇨🇿": "1-1",
    "21) 🏴󠁧󠁢󠁥󠁮󠁧󠁿England vs Scotland 🏴󠁧󠁢󠁳󠁣󠁴󠁿": "0-0",
    "22) 🇭🇺Hungary vs France 🇫🇷": "1-1",
    "23) 🇵🇹Portugal vs Germany 🇩🇪": "2-4",
    "24) 🇪🇸Spain vs Poland 🇵🇱": "1-1",
    "25) 🇮🇹Italy vs Wales 🏴󠁧󠁢󠁷󠁬󠁳󠁿": "1-0",
    "26)🇨🇭Switzerland vs Turkey 🇹🇷": "3-1",
    "27) 🇲🇰North Macedonia vs Netherlands 🇳🇱": "0-3",
    "28) 🇺🇦Ukraine vs Austria 🦘": "0-1",
    "29) 🇷🇺Russia vs Denmark 🇩🇰": "1-4",
    "30) 🇫🇮Finland vs Belgium 🇧🇪": "0-2"
}

BETS_DIR = "bets"
PLAYER_NAME_INDEX = 1
GAMES_START_FROM_INDEX = 2

EXACT_SCORE_PREDICTED_POINTS = 3
GOAL_DIFFERENCE_PREDICTED_POINTS = 2
OUTCOME_PREDICTED_POINTS = 1
WRONG_PREDICTION_POINTS = 0

# How we count points:
# - exact score: 3 pts
# - goal difference: 2pts
# - outcome (win or lose): 1pt
#
# Example:
# Say, the final result of the game: 1-0
# - you voted 1-0 -> 3 points (exact score, goal diff, outcome - everything is correct)
# - you voted 2-1 -> 2 points (goal difference and outcome are correct)
# - you voted 3-1 -> 1 pint (outcome is correct)
# - you voted 1-1 -> 0

signum = lambda x: -1 if x < 0 else (1 if x > 0 else 0)


def count_points(expected: str, actual: str) -> int:
    """
    e.g: expected: "2-1", actual: "1-0" -> points: 2
    """
    actual = actual.strip()
    expected = expected.strip()
    assert actual != "" and expected != ""

    if actual == expected:
        return EXACT_SCORE_PREDICTED_POINTS

    left_expected, right_expected = list(map(lambda x: int(x), expected.split('-')))
    goal_diff_expected = left_expected - right_expected

    left_actual, right_actual = list(map(lambda x: int(x), actual.split('-')))
    goal_diff_actual = left_actual - right_actual

    if goal_diff_expected == goal_diff_actual:
        return GOAL_DIFFERENCE_PREDICTED_POINTS

    if signum(goal_diff_expected) == signum(goal_diff_actual):
        return OUTCOME_PREDICTED_POINTS

    return WRONG_PREDICTION_POINTS


def main():
    game_index = dict()
    player_points = dict()

    for root, dirs, files in os.walk(BETS_DIR):
        for file in files:
            # remove already seen games
            game_index.clear()

            with open(os.path.join(root, file)) as f:
                print(f'\n-> Parsing {f.name}\n')
                reader = csv.reader(f, delimiter=',', quotechar='"')

                # scan header to extract games from the current csv
                is_header = True
                for row in reader:
                    if is_header:
                        is_header = False
                        # names of the games start from 2nd column in csv: timestamp,email,game1,game2,game3,...gameN
                        for i in range(GAMES_START_FROM_INDEX, len(row)):
                            name_of_the_game = row[i]
                            # match each game with it's column's index in the csv: (game1 -> 2, game2 -> 3, ...)
                            game_index[name_of_the_game] = i
                        continue

                    # don't forget to remove emails from csv before processing
                    player = row[PLAYER_NAME_INDEX]
                    if player not in player_points:
                        player_points[player] = 0

                    # for each game that was scanned previously, compare actual score against prediction
                    print(f'Player: {player}')
                    points_aggregated = 0
                    for game, index in game_index.items():
                        predicted_result = row[index]
                        actual_result = GAME_RESULTS[game]
                        if actual_result == "":
                            print(f"  game: {game} has not finished yet")
                            continue

                        points = count_points(predicted_result, actual_result)
                        print(f'  game: {game}\tpredicted: {predicted_result}, '
                              f'actual: {actual_result} -> points: {points}')

                        points_aggregated += points

                    print(f'  score -> {points_aggregated}')
                    player_points[player] += points_aggregated

    # sort by player name (key of the dict)
    player_points_sorted = dict(sorted(player_points.items(), key=lambda item: item[0]))

    # save as csv
    today = date.today().strftime("%b-%d")
    with open(f"points/player-points-{today}.csv", 'w') as f:
        writer = csv.writer(f, delimiter=',')

        writer.writerow(['Player', 'Points'])
        print('\nResults:')
        for player, points in player_points_sorted.items():
            print(f'{player} \t -> {points}')
            writer.writerow([player, points])


if __name__ == '__main__':
    assert EXACT_SCORE_PREDICTED_POINTS == count_points(expected='1-1', actual='1-1')
    assert EXACT_SCORE_PREDICTED_POINTS == count_points(expected='1-3', actual='1-3')
    assert EXACT_SCORE_PREDICTED_POINTS == count_points(expected='3-1', actual='3-1')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='1-3', actual='0-2')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='1-3', actual='3-5')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-1', actual='2-0')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-1', actual='4-2')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-3', actual='0-0')
    assert GOAL_DIFFERENCE_PREDICTED_POINTS == count_points(expected='3-3', actual='1-1')
    assert OUTCOME_PREDICTED_POINTS == count_points(expected='3-1', actual='1-0')
    assert OUTCOME_PREDICTED_POINTS == count_points(expected='3-1', actual='4-0')
    assert OUTCOME_PREDICTED_POINTS == count_points(expected='1-3', actual='4-5')
    assert WRONG_PREDICTION_POINTS == count_points(expected='1-3', actual='3-1')
    assert WRONG_PREDICTION_POINTS == count_points(expected='1-3', actual='1-1')
    assert WRONG_PREDICTION_POINTS == count_points(expected='3-1', actual='1-1')
    assert WRONG_PREDICTION_POINTS == count_points(expected='3-3', actual='2-3')
    assert WRONG_PREDICTION_POINTS == count_points(expected='3-3', actual='3-2')
    main()
