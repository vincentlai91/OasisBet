package com.oasisbet.result.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oasisbet.result.model.ResultApiResponse;
import com.oasisbet.result.model.ResultEvent;
import com.oasisbet.result.model.Score;

@Service
public class ResultService {
	public List<ResultEvent> processMapping(ResultApiResponse[] results) throws ParseException {
		List<ResultEvent> resultEventList = new ArrayList<>();
		for (ResultApiResponse result : results) {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			String dateString = result.getCommence_time();
			Date startTime = dateFormat.parse(dateString);

			if (result.isCompleted()) {
				String lastUpdatedString = result.getLast_update();
				Date lastUpdated = lastUpdatedString != null ? dateFormat.parse(lastUpdatedString) : null;

				// Convert to SG time - add 8 hours to the start time
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(startTime);
				calendar.add(Calendar.HOUR_OF_DAY, 8);
				startTime = calendar.getTime();

				if (lastUpdated != null) {
					calendar.setTime(lastUpdated);
					calendar.add(Calendar.HOUR_OF_DAY, 8);
					lastUpdated = calendar.getTime();
				}

				String eventDesc = result.getHome_team() + " vs " + result.getAway_team();
				String competition = result.getSport_title();
				boolean completed = result.isCompleted();
				String homeTeam = result.getHome_team();
				String awayTeam = result.getAway_team();

				List<Score> scoreList = result.getScores();
				String score = "";
				if (scoreList != null && scoreList.size() > 1) {
					Score homeScore = scoreList.get(0).getName().equals(result.getHome_team()) ? scoreList.get(0)
							: scoreList.get(1);
					Score awayScore = scoreList.get(1).getName().equals(result.getAway_team()) ? scoreList.get(1)
							: scoreList.get(0);
					score = homeScore.getScore() + "-" + awayScore.getScore();
				}

				ResultEvent event = new ResultEvent(1000, competition, eventDesc, startTime, completed, homeTeam,
						awayTeam, score, lastUpdated);
				resultEventList.add(event);
			}

		}

		resultEventList = resultEventList.stream().sorted(Comparator.comparing(ResultEvent::getStartTime))
				.collect(Collectors.toList());
		return resultEventList;
	}
}
